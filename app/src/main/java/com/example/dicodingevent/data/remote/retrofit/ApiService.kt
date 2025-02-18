package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.EventDetailResponse
import com.example.dicodingevent.data.remote.response.EventListResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("/events")
    suspend fun getEvents(
        @Query("active") active: Int = 1,
        @Query("q") q: String? = null,
        @Query("limit") limit: Int = 40
    ): EventListResponse

    @GET("/events/{id}")
    suspend fun getEventDetail(@Path("id") id: String): EventDetailResponse
}