package com.example.uf1_proyecto_sonidos.ui.event

import com.example.uf1_proyecto_sonidos.data.database.entities.Category

sealed interface CategoryEvent {
    data class SaveCategory(val category: Category): CategoryEvent
    data class DeleteCategory(val category: Category): CategoryEvent
    data class SelectCategory(val categoryId: Int): CategoryEvent
    data class SetName(val name: String): CategoryEvent
    object ShowCategoryDialog: CategoryEvent
    object HideCategoryDialog: CategoryEvent
}