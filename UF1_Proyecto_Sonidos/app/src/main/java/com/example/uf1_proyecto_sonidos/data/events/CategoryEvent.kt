package com.example.uf1_proyecto_sonidos.data.events

import com.example.uf1_proyecto_sonidos.data.database.entities.Category

sealed interface CategoryEvent {
    data class SaveCategory(val category: Category): CategoryEvent
    data class DeleteCategory(val category: Category): CategoryEvent
    data class SelectCategory(val categoryId: Int): CategoryEvent
    object GetAllCategories : CategoryEvent
}