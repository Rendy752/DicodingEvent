package com.example.dicodingevent.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class ApiResponse<T>(
    val error: Boolean,
    val message: String,
    @JsonNames("listEvents", "event")
    val data: T? = null
)