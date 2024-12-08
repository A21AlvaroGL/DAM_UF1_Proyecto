package com.example.uf1_proyecto_sonidos.data.view_models

import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import com.example.uf1_proyecto_sonidos.data.sort_types.SoundSortType

// Representa los estados que pueden tener los sonidos
data class SoundState (
    val sounds: List<Sound> = emptyList(),
    val name: String = "",
    val path: String = "",
    val favorite: Boolean = false,
    val sortType: SoundSortType = SoundSortType.NAME,
    val categoryId: Int? = null
)