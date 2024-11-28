package com.example.uf1_proyecto_sonidos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uf1_proyecto_sonidos.data.database.daos.SoundDAO
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.ui.SoundSortType
import com.example.uf1_proyecto_sonidos.ui.event.SoundEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        }

    fun onEvent(event: SoundEvent) {
        when(event) {
            is SoundEvent.SaveSound -> TODO()
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