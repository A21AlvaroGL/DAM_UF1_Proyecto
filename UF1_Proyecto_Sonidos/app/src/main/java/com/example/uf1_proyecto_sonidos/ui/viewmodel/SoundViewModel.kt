package com.example.uf1_proyecto_sonidos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uf1_proyecto_sonidos.data.database.daos.SoundDAO
import com.example.uf1_proyecto_sonidos.ui.SortType
import com.example.uf1_proyecto_sonidos.ui.event.SoundEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SoundViewModel (
    private val dao: SoundDAO
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.NAME)
    private val _sounds = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.NAME -> dao.getSoundsOrderedByNameASC()
                SortType.TIMESTAMP -> dao.getRecentSounds()
                SortType.CATEGORY -> TODO()
            }
        }
    private val _state = MutableStateFlow(SoundState())

    fun onEvent(event: SoundEvent) {
        when(event) {
            is SoundEvent.DeleteSound -> {
                viewModelScope.launch {
                    dao.deleteSound(event.sound)
                }
            }

            SoundEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingSound = false
                ) }
            }
            SoundEvent.SaveSound -> {

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
            is SoundEvent.SortSounds -> {
                _sortType.value = event.SortType
            }
            SoundEvent.showDialog -> {
                _state.update {
                    it.copy(
                        isAddingSound = true
                    )
                }
            }
        }
    }
}