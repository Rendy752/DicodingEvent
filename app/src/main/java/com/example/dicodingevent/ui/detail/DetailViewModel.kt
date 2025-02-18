package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.repository.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadEvents(id: String?) {
        if (id != null) {
            getDetail(id)
        }
    }

    private fun getDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val fetchedEvent = repository.getEventDetail(id) ?: throw Exception("Event not found")
                _event.value = fetchedEvent
            } catch (e: Exception) {
                println("Error loading event detail: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteFavoriteEvent(event: Event) {
        viewModelScope.launch {
            repository.deleteFavoriteEvent(event)
            _isFavorite.value = repository.isFavorite(event.id)
        }
    }

    fun insertFavoriteEvent(event: Event) {
        viewModelScope.launch {
            repository.insertFavoriteEvent(event)
            _isFavorite.value = repository.isFavorite(event.id)
        }
    }

    fun isFavorite(id: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.isFavorite(id)
        }
    }
}