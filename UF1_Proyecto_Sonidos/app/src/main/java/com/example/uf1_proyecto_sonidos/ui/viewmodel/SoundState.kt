package com.example.uf1_proyecto_sonidos.ui.viewmodel

import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.ui.SortType

data class SoundState (
    val sounds: List<Sound> = emptyList(),
    val name: String = "",
    val path: String = "",
    val favorite: Boolean = false,
    val isAddingSound: Boolean = false,
    val sortType: SortType = SortType.NAME
)