package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<Event>>()
    val upcomingEvents: LiveData<List<Event>> = _upcomingEvents
    private val _errorMessageUpcoming = MutableStateFlow<String?>(null)
    val errorMessageUpcoming: StateFlow<String?> = _errorMessageUpcoming.asStateFlow()
    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> = _finishedEvents
    private val _errorMessageFinished = MutableStateFlow<String?>(null)
    val errorMessageFinished: StateFlow<String?> = _errorMessageFinished.asStateFlow()

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming
    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    init {
        loadUpcomingEvents()
        loadFinishedEvents()
    }

    private fun loadUpcomingEvents() {
        _isLoadingUpcoming.value = true
        viewModelScope.launch {
            try {
                val fetchedEvents = repository.getUpcomingEvents(limit = 5)
                _upcomingEvents.value = fetchedEvents
            } catch (e: Exception) {
                _errorMessageUpcoming.value = e.message ?: "Unknown error"
                _upcomingEvents.value = emptyList()
            } finally {
                _isLoadingUpcoming.value = false
            }
        }
    }

    private fun loadFinishedEvents() {
        _isLoadingFinished.value = true
        viewModelScope.launch {
            try {
                val fetchedEvents = repository.getFinishedEvents(limit = 5)
                _finishedEvents.value = fetchedEvents
            } catch (e: Exception) {
                _errorMessageFinished.value = e.message ?: "Unknown error"
                _finishedEvents.value = emptyList()
            } finally {
                _isLoadingFinished.value = false
            }
        }
    }

    fun resetErrorMessageUpcoming() {
        _errorMessageUpcoming.value = null
    }

    fun resetErrorMessageFinished() {
        _errorMessageFinished.value = null
    }
}