package com.example.uf1_proyecto_sonidos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uf1_proyecto_sonidos.data.database.daos.CategoryDAO
import com.example.uf1_proyecto_sonidos.ui.CategorySortType
import com.example.uf1_proyecto_sonidos.ui.event.CategoryEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryViewModel (
    private val dao: CategoryDAO
): ViewModel() {

    private val _sortType = MutableStateFlow(CategorySortType.NAME)
    private val _state = MutableStateFlow(CategoryState())
    private val _categories = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                CategorySortType.NAME -> dao.getCategoriesOrderedByName()
            }
        }

    fun onEvent(event: CategoryEvent) {
        when(event) {
            is CategoryEvent.SaveCategory -> TODO()
            is CategoryEvent.DeleteCategory -> {
                viewModelScope.launch {
                    dao.deleteCategory(event.category)
                }
            }
            is CategoryEvent.SetName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            is CategoryEvent.SelectCategory -> TODO()
            CategoryEvent.HideCategoryDialog -> {
                _state.update {
                    it.copy(
                        isAddingCategory = false
                    )
                }
            }
            CategoryEvent.ShowCategoryDialog -> {
                _state.update {
                    it.copy(
                        isAddingCategory = true
                    )
                }
            }
        }
    }
}