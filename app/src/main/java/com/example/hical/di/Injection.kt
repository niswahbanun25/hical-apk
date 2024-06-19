package com.example.hical.di

import android.content.Context
import com.example.hical.api.ApiConfig
import com.example.hical.preference.UserPreference
import com.example.hical.preference.dataStore
import com.example.hical.repository.RetrofitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRetrofitRepository(context: Context): RetrofitRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val token = runBlocking { pref.getSession().first().token }
        val apiService = ApiConfig.getApiService(token)
        return RetrofitRepository(apiService, pref)
    }
}