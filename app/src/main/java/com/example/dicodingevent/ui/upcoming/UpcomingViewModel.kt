package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.models.Event
import com.example.dicodingevent.repository.EventRepository
import kotlinx.coroutines.launch

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadEvents()
    }

    fun loadEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedEvents = repository.getUpcomingEvents()
                Log.d("UpcomingViewModel", "Fetched events: $fetchedEvents")
                _events.value = fetchedEvents
            } catch (e: Exception) {
                Log.e("UpcomingViewModel", "Error loading events", e)
                println("Error loading events: ${e.message}")
                _events.value = emptyList()
            } finally {
                Log.d("UpcomingViewModel", "Finished loading events")
                _isLoading.value = false
            }
        }
    }
}