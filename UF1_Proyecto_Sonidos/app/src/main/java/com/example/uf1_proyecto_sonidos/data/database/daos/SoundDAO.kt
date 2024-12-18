package com.example.uf1_proyecto_sonidos.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.uf1_proyecto_sonidos.data.database.entities.Sound
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundDAO {

    @Upsert
    fun upsertSound(sound: Sound)

    @Delete
    suspend fun deleteSound(sound: Sound)

    @Query("DELETE FROM sounds WHERE id = :soundId")
    suspend fun deleteSoundById(soundId: Int)

    @Query("SELECT * FROM sounds ORDER BY timestamp DESC")
    fun getRecentSounds(): Flow<List<Sound>>

    @Query("SELECT * FROM sounds ORDER BY timestamp ASC")
    fun getOldestSounds(): Flow<List<Sound>>

    @Query("SELECT * FROM sounds ORDER BY name ASC")
    fun getSoundsOrderedByNameASC(): Flow<List<Sound>>

    @Query("SELECT * FROM sounds ORDER BY name DESC")
    fun getSoundsOrderedByNameDESC(): Flow<List<Sound>>

    @Query("SELECT * FROM sounds WHERE favorite == 1")
    fun getFavoritesSounds(): Flow<List<Sound>>

    @Query("SELECT * FROM sounds WHERE category_id = :categoryId")
    fun getSoundsByCategory(categoryId: Int): Flow<List<Sound>>
}