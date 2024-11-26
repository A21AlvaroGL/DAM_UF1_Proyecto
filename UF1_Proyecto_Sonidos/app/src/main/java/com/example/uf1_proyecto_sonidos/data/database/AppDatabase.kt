package com.example.uf1_proyecto_sonidos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Sound::class, Category::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun soundDAO(): SoundDAO
    abstract fun categoryDAO(): CategoryDAO
}