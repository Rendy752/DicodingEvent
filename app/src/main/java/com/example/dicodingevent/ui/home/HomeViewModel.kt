package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.models.Event
import com.example.dicodingevent.repository.EventRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<Event>>()
    val upcomingEvents: LiveData<List<Event>> = _upcomingEvents
    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> = _finishedEvents

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
                val fetchedEvents = repository.getUpcomingEvents()
                _upcomingEvents.value = fetchedEvents
            } catch (e: Exception) {
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
                val fetchedEvents = repository.getFinishedEvents()
                _finishedEvents.value = fetchedEvents
            } catch (e: Exception) {
                _finishedEvents.value = emptyList()
            } finally {
                _isLoadingFinished.value = false
            }
        }
    }
}