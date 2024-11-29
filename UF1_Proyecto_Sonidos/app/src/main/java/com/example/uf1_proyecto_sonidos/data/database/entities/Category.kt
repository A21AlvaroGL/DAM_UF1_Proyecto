package com.example.uf1_proyecto_sonidos.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)