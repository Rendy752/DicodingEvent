package com.example.dicodingevent.ui.finished

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.di.Injection
import com.example.dicodingevent.repository.EventRepository

class FinishedViewModelFactory private constructor(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinishedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: FinishedViewModelFactory? = null
        fun getInstance(context: Context): FinishedViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FinishedViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}