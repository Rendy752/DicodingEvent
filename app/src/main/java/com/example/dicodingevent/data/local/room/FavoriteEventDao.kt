package com.example.dicodingevent.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import androidx.room.Query
import com.example.dicodingevent.data.local.entity.FavoriteEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun delete(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavoriteEvents(): Flow<List<FavoriteEvent>>

    @Query("SELECT EXISTS(SELECT 1 FROM FavoriteEvent WHERE id =:id)")
    suspend fun isFavorite(id: Int): Boolean
}