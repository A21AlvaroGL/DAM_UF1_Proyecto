package com.example.uf1_proyecto_sonidos.data.view_models

import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import com.example.uf1_proyecto_sonidos.data.sort_types.CategorySortType

// Representa los estados que pueden tener las categorías
data class CategoryState (
    val categories: List<Category> = emptyList(),
    val name: String = "",
    val sortType: CategorySortType = CategorySortType.NAME,
    val selectedCategoryId: Int? = null
)