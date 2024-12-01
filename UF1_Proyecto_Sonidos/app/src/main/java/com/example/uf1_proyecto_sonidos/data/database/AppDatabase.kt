package com.example.uf1_proyecto_sonidos.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uf1_proyecto_sonidos.data.database.daos.CategoryDAO
import com.example.uf1_proyecto_sonidos.data.database.daos.SoundDAO
import com.example.uf1_proyecto_sonidos.data.database.entities.Category
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound

@Database(
    entities = [Sound::class, Category::class],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun soundDAO(): SoundDAO
    abstract fun categoryDAO(): CategoryDAO

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "avisonus")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}