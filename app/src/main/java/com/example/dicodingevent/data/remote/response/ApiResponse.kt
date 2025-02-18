package com.example.dicodingevent.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class EventListResponse(
    val error: Boolean,
    val message: String,
    val listEvents: List<Event>
)

@Serializable
data class EventDetailResponse(
    val error: Boolean,
    val message: String,
    val event: Event
)