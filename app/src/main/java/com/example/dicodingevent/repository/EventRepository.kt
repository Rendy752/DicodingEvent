package com.example.dicodingevent.repository

import com.example.dicodingevent.data.ApiService
import com.example.dicodingevent.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepository @Inject constructor(private val apiService: ApiService) {

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
}