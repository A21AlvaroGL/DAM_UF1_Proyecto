package com.example.uf1_proyecto_sonidos.ui.viewmodel

import com.example.uf1_proyecto_sonidos.data.database.entities.Category

data class CategoryState (
    val categories: List<Category> = emptyList(),
    val name: String = "",
    val isAddingCategory: Boolean = false,
)