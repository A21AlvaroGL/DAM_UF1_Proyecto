package com.example.uf1_proyecto_sonidos.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Upsert
    fun upsertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(categoryId: Int)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getCategoriesOrderedByName(): Flow<List<Category>>
}