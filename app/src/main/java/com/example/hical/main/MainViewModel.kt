package com.example.hical.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hical.preference.UserModel
import com.example.hical.repository.RetrofitRepository

class MainViewModel (private val retrofitRepository: RetrofitRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return retrofitRepository.getSession().asLiveData()
    }
}