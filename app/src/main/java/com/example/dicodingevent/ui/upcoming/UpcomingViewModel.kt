package com.example.dicodingevent.ui.upcoming

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

    private fun loadEvents() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedEvents = repository.getUpcomingEvents()
                _events.value = fetchedEvents
            } catch (e: Exception) {
                _events.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}