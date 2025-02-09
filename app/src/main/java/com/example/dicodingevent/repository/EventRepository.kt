package com.example.dicodingevent.repository

import com.example.dicodingevent.data.ApiService
import com.example.dicodingevent.models.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getUpcomingEvents(): List<Event> = withContext(Dispatchers.IO) {
        return@withContext getEventsFromApi(1)
    }

    suspend fun getFinishedEvents(): List<Event> = withContext(Dispatchers.IO) {
        return@withContext getEventsFromApi(0)
    }

    private suspend fun getEventsFromApi(active: Int): List<Event> {
        try {
            val response = apiService.getEvents(active = active)
            if (!response.error) {
                return response.listEvents
            } else {
                println("API Error: ${response.message}")
                return emptyList()
            }
        } catch (e: Exception) {
            println("Network Error: ${e.message}")
            return emptyList()
        }
    }


    suspend fun getEventDetail(id: Int): Event? = withContext(Dispatchers.IO) {
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