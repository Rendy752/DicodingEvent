package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.local.room.FavoriteEventDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.repository.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.apiService
        val database = FavoriteEventDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        return EventRepository(apiService, dao)
    }
}