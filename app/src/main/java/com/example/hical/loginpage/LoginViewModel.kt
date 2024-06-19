package com.example.hical.loginpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hical.preference.UserModel
import com.example.hical.repository.RetrofitRepository
import com.example.hical.response.ErrorResponse
import com.example.hical.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val retrofitRepository: RetrofitRepository
) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            retrofitRepository.saveSession(user)
        }
    }
    suspend fun login(email: String, password: String): LoginResponse {
        try {
            val response = retrofitRepository.login(email, password)
            Log.d("RegisterProcess", "The registration process was successful: $response")
            response.let { loginResponse ->
                val user = UserModel(email, loginResponse.token, true)
                retrofitRepository.saveSession(user)
            }
            return response
        } catch (e: Exception) {
            val jsonInString = (e as HttpException).response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.e("RegisterProcess", "The registration process failed: $errorMessage")
            throw e
        }
    }
}