package com.example.uf1_proyecto_sonidos.data.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uf1_proyecto_sonidos.data.database.daos.SoundDAO
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.data.sort_types.SoundSortType
import com.example.uf1_proyecto_sonidos.data.events.SoundEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SoundViewModel (
    private val dao: SoundDAO
): ViewModel() {

    private val _sortType = MutableStateFlow(SoundSortType.NAME)
    private val _state = MutableStateFlow(SoundState())
    private val _sounds = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SoundSortType.NAME -> dao.getSoundsOrderedByNameASC()
                SoundSortType.TIMESTAMP -> dao.getRecentSounds()
                SoundSortType.CATEGORY -> {
                    val categoryId = _state.value.categoryId
                    if (categoryId != null) {
                        dao.getSoundsByCategory(categoryId)
                    } else {
                        flowOf(emptyList<Sound>())
                    }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _sortType, _sounds) { state, sortType, sounds ->
         state.copy(
            sounds = sounds,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SoundState())

    fun onEvent(event: SoundEvent) {
        when(event) {
            is SoundEvent.SaveSound -> {
                val name = state.value.name
                val path = state.value.path
                val categoryId = state.value.categoryId

                if (name.isBlank() || path.isBlank()) {
                    return
                }
                
                val sound = Sound(
                    name = name,
                    path = path,
                    category_id = categoryId
                )

                viewModelScope.launch {
                    dao.upsertSound(sound)
                }

                _state.update {
                    it.copy(
                        isAddingSound = false,
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
            is SoundEvent.SetFavorite -> {
                _state.update {
                    it.copy(
                        favorite = event.favorite
                    )
                }
            }
            is SoundEvent.SetName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            is SoundEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingSound = false
                ) }
            }
            SoundEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingSound = true
                    )
                }
            }
            is SoundEvent.SortSounds -> {
                _sortType.value = event.SortType
            }
            is SoundEvent.FilterByCategory -> {
                _sortType
            }
        }
    }
}