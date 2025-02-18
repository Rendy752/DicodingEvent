package com.example.dicodingevent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dicodingevent.data.remote.response.Event

@Entity
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val summary: String,
    val mediaCover: String
)

fun FavoriteEvent.toEvent(): Event {
    return Event(
        id = this.id,
        name = this.name,
        summary = this.summary,
        mediaCover = this.mediaCover,
        description = "",
        imageLogo = "",
        category = "",
        ownerName = "",
        cityName = "",
        quota = 0,
        registrants = 0,
        beginTime = "",
        endTime = "",
        link = ""
    )
}