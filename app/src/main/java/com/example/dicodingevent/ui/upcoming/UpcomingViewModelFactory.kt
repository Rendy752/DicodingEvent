package com.example.dicodingevent.ui.upcoming

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.di.Injection
import com.example.dicodingevent.repository.EventRepository

class UpcomingViewModelFactory private constructor(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpcomingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: UpcomingViewModelFactory? = null
        fun getInstance(context: Context): UpcomingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UpcomingViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}