package com.example.uf1_proyecto_sonidos.data.events

import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.data.sort_types.SoundSortType
// Define los eventos que pueden tener los sonidos
sealed interface SoundEvent {
    data class SaveSound(val sound: Sound): SoundEvent
    data class SetFavorite(val favorite: Boolean): SoundEvent
    data class SortSounds(val SortType: SoundSortType): SoundEvent
    data class DeleteSound(val sound: Sound): SoundEvent
    data class DeleteSoundById(val id: Int): SoundEvent
    data class FilterByCategory(val categoryId: Int): SoundEvent
}