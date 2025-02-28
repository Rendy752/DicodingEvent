package com.example.dicodingevent.data.remote.response

import com.example.dicodingevent.data.local.entity.FavoriteEvent
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val name: String,
    val summary: String,
    val description: String,
    val imageLogo: String,
    val mediaCover: String,
    val category: String,
    val ownerName: String,
    val cityName: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val endTime: String,
    val link: String
)

fun Event.toFavoriteEvent(): FavoriteEvent {
    return FavoriteEvent(
        id = this.id,
        name = this.name,
        summary = this.summary,
        mediaCover = this.mediaCover
    )
}