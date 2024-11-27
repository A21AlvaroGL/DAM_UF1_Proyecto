package com.example.uf1_proyecto_sonidos.ui.event

import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.ui.SortType

sealed interface SoundEvent {
    object SaveSound: SoundEvent
    data class  SetName(val name: String): SoundEvent
    data class  SetFavorite(val favorite: Boolean): SoundEvent
    object showDialog: SoundEvent
    object HideDialog: SoundEvent
    data class SortSounds(val SortType: SortType): SoundEvent
    data class DeleteSound(val sound: Sound): SoundEvent
}