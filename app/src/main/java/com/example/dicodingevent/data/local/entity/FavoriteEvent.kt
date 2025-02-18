package com.example.dicodingevent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dicodingevent.data.remote.response.Event

@Entity
data class FavoriteEvent(
    @PrimaryKey
    val id: Int,
    val name: String,
    val summary: String,
    val mediaCover: String
)

fun Event.toFavoriteEvent(): FavoriteEvent {
    return FavoriteEvent(
        id = this.id,
        name = this.name,
        summary = this.summary,
        mediaCover = this.mediaCover
    )
}