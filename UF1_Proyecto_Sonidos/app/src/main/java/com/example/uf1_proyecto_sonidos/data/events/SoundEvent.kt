package com.example.uf1_proyecto_sonidos.data.events

import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.data.sort_types.SoundSortType

sealed interface SoundEvent {
    data class SaveSound(val sound: Sound): SoundEvent
    data class SetName(val name: String): SoundEvent
    data class SetFavorite(val favorite: Boolean): SoundEvent
    data class SortSounds(val SortType: SoundSortType): SoundEvent
    data class DeleteSound(val sound: Sound): SoundEvent
    data class FilterByCategory(val categoryId: Int): SoundEvent
    object ShowDialog: SoundEvent
    object HideDialog: SoundEvent
}