package com.example.uf1_proyecto_sonidos.data.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uf1_proyecto_sonidos.data.database.daos.SoundDAO
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.data.sort_types.SoundSortType
import com.example.uf1_proyecto_sonidos.data.events.SoundEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SoundViewModel (
    private val dao: SoundDAO
): ViewModel() {

    private val _sortType = MutableStateFlow(SoundSortType.CATEGORY)
    private val _state = MutableStateFlow(SoundState())
    private val _sounds = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SoundSortType.NAME -> dao.getSoundsOrderedByNameASC()
                SoundSortType.TIMESTAMP -> dao.getRecentSounds()
                SoundSortType.CATEGORY -> {
                    val categoryId = _state.value.categoryId
                    if (categoryId != null) {
                        dao.getSoundsByCategory(14)
                    } else {
                        dao.getRecentSounds()
                    }
                }
            }
        }
    /* state combina los valores de _state, _sortType y _sounds.
    Cuando uno de estos valos cambia se genera un nuevo valor de state */
    val state = combine(_state, _sortType, _sounds) { state, sortType, sounds ->
         state.copy(
            sounds = sounds,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SoundState())
    // Esta función identifica que tipo de evento se ha enviado y ejecutar una acción en función a ello.
    fun onEvent(event: SoundEvent) {
        when(event) {
            is SoundEvent.SaveSound -> {
                val name = event.sound.name
                val path = event.sound.path
                val categoryId = event.sound.category_id

                if (categoryId != null) {
                    if (name.isBlank() || path.isBlank() || categoryId <= 0 ) {
                        return
                    }
                }

                val sound = Sound(
                    name = name,
                    path = path,
                    category_id = categoryId
                )

                // Es necesario usar Dispatchers porque si no la inserción se hace en el hilo principal y da un error
                viewModelScope.launch(Dispatchers.IO) {
                    dao.upsertSound(sound)
                }

                _state.update {
                    it.copy(
                        name = "",
                        path = "",
                        categoryId = null
                    )
                }
            }
            is SoundEvent.DeleteSound -> {
                viewModelScope.launch {
                    dao.deleteSound(event.sound)
                }
            }
            is SoundEvent.DeleteSoundById -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteSoundById(event.id)
                }
            }
            is SoundEvent.SetFavorite -> {
                _state.update {
                    it.copy(
                        favorite = event.favorite
                    )
                }
            }
            is SoundEvent.SortSounds -> {
                _sortType.value = event.SortType
            }
            is SoundEvent.FilterByCategory -> {
                /* En log si que aparece el id de la categoría seleccionada.
                pero al actualziar state el filtrado no funciona */
                Log.d("SoundViewModel", "Filtering by category, categoryId: ${event.categoryId}")
                _state.update { currentState ->
                    _sortType.value = SoundSortType.CATEGORY
                    currentState.copy(categoryId = event.categoryId)
                }
            }
        }
    }
}