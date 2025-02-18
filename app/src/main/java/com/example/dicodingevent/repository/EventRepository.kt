package com.example.dicodingevent.repository

import com.example.dicodingevent.data.local.entity.FavoriteEvent
import com.example.dicodingevent.data.local.entity.toFavoriteEvent
import com.example.dicodingevent.data.local.room.FavoriteEventDao
import com.example.dicodingevent.data.remote.retrofit.ApiService
import com.example.dicodingevent.data.remote.response.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepository(private val apiService: ApiService, private val favoriteEventDao: FavoriteEventDao) {

    suspend fun getUpcomingEvents(query: String? = null, limit: Int? = 40): List<Event> =
        getEventsFromApi(active = 1, query = query, limit = limit)

    suspend fun getFinishedEvents(query: String? = null, limit: Int? = 40): List<Event> =
        getEventsFromApi(active = 0, query = query, limit = limit)

    private suspend fun getEventsFromApi(
        active: Int,
        query: String? = null,
        limit: Int? = 40
    ): List<Event> = withContext(Dispatchers.IO) {
        try {
            val response =
                apiService.getEvents(active = active, q = query, limit = limit ?: 40)
            if (response.error) {
                println("API Error: ${response.message}")
                return@withContext emptyList()
            }
            return@withContext response.listEvents
        } catch (e: Exception) {
            println("Network Error: ${e.message}")
            return@withContext emptyList()
        }
    }

    suspend fun getEventDetail(id: String): Event? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getEventDetail(id)
            if (!response.error) {
                return@withContext response.event
            } else {
                println("API Error: ${response.message}")
                return@withContext null
            }
        } catch (e: Exception) {
            println("Network Error: ${e.message}")
            return@withContext null
        }
    }

    suspend fun insertFavoriteEvent(event: Event) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.insert(event.toFavoriteEvent())
        }
    }

    suspend fun deleteFavoriteEvent(event: Event) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.delete(event.toFavoriteEvent())
        }
    }

    fun getAllFavoriteEvents(): Flow<List<FavoriteEvent>> {
        return favoriteEventDao.getAllFavoriteEvents()
    }

    suspend fun isFavorite(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.isFavorite(id)
        }
    }
}