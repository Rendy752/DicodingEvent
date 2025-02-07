package com.example.dicodingevent.data

import com.example.dicodingevent.models.ApiResponse
import com.example.dicodingevent.models.Event
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("/events")
    suspend fun getEvents(
        @Query("active") active: Int = 1,
        @Query("q") q: String? = null,
        @Query("limit") limit: Int = 40
    ): ApiResponse<List<Event>>

    @GET("/events/{id}")
    suspend fun getEventDetail(@Path("id") id: Int): ApiResponse<Event>
}