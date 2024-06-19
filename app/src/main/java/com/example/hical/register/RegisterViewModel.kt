package com.example.hical.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hical.repository.RetrofitRepository
import com.example.hical.response.RegisterResponse

class RegisterViewModel (private val retrofitrepository: RetrofitRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        try {
            val response = retrofitrepository.register(name, email, password)
            Log.d("RegisterProcess", "The registration process was successful: $response")
            return response
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An error occurred on the server."
            Log.e("RegisterProcess", "The registration process failed: $errorMessage")
            throw e
        }
    }
}