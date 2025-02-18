package com.example.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _queryState = MutableStateFlow(QueryState())
    val queryState: StateFlow<QueryState> = _queryState.asStateFlow()

    data class QueryState(val query: String = "")

    init {
        loadEvents()
    }

    fun updateQuery(query: String) {
        _queryState.value = queryState.value.copy(query = query)
        searchEvents(query)
    }

    private fun loadEvents() {
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

    fun searchEvents(query: String?) {
        _queryState.update { it.copy(query = query ?: "") }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedEvents = repository.getFinishedEvents(query = query)
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