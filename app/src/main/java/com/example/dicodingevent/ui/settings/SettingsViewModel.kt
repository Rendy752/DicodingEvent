package com.example.dicodingevent.ui.settings

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.repository.EventRepository

class SettingsViewModel(private val repository: EventRepository) : ViewModel() {
    suspend fun getNewEvent(): Event? {
        try {
            return repository.getNewEvent()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}