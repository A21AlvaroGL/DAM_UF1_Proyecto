package com.example.uf1_proyecto_sonidos.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDAO {

    @Upsert
    fun upsertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getCategoriesOrderedByName(): Flow<List<Category>>
}