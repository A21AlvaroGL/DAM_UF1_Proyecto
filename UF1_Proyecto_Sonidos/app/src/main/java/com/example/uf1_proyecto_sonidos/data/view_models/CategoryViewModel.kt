package com.example.uf1_proyecto_sonidos.data.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uf1_proyecto_sonidos.data.database.daos.CategoryDAO
import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import com.example.uf1_proyecto_sonidos.data.sort_types.CategorySortType
import com.example.uf1_proyecto_sonidos.data.events.CategoryEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModel (
    private val dao: CategoryDAO
): ViewModel() {

    private val _sortType = MutableStateFlow(CategorySortType.NAME)
    private val _categories = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                CategorySortType.NAME -> dao.getCategoriesOrderedByName()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(CategoryState())
    val state = combine(_state, _sortType, _categories) { state, sortType, categories ->
        state.copy(
            categories = categories,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryState())

    fun onEvent(event: CategoryEvent) {
        when(event) {
            is CategoryEvent.SaveCategory -> {
                val name = event.category.name

                if (name.isBlank()) {
                    return
                }

                val category = Category(
                    name = event.category.name
                )

                // Es necesario usar Dispatchers porque si no la inserción se ejecuta en el hilo principal y da un error
                viewModelScope.launch(Dispatchers.IO) {
                    dao.upsertCategory(category)
                }

                _state.update { it.copy(
                    name = ""
                )}
            }
            is CategoryEvent.DeleteCategory -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.deleteCategory(event.category)
                }
            }
            is CategoryEvent.SelectCategory -> {
                _state.update {
                    it.copy(
                        selectedCategoryId = event.categoryId
                    )
                }
            }
            is CategoryEvent.GetAllCategories -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dao.getCategoriesOrderedByName()
                        .collect { categories ->
                            _state.update {
                                it.copy(
                                    categories = categories
                                )
                            }
                        }
                }
            }
        }
    }
}