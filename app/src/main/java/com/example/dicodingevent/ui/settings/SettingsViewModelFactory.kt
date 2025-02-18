package com.example.dicodingevent.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.di.Injection
import com.example.dicodingevent.repository.EventRepository

class SettingsViewModelFactory private constructor(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: SettingsViewModelFactory? = null
        fun getInstance(context: Context): SettingsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SettingsViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}