package com.example.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.models.Event
import com.example.dicodingevent.repository.EventRepository
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

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
                val fetchedEvents = repository.getFinishedEvents()
                _events.value = fetchedEvents
            } catch (e: Exception) {
                println("Error loading events: ${e.message}")
                _events.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}