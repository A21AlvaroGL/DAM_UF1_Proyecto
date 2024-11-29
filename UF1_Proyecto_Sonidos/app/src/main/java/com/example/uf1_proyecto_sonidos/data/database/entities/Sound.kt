package com.example.uf1_proyecto_sonidos.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "sounds",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        childColumns = ["category_id"],
        parentColumns = ["id"],
        onDelete = ForeignKey.SET_NULL
)])
data class Sound (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val path: String,
    val favorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val category_id: Int?
)