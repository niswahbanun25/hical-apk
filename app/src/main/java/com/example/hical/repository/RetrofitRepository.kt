package com.example.hical.repository

import androidx.lifecycle.liveData
import com.example.hical.api.ApiService
import com.example.hical.api.Result
import com.example.hical.preference.UserModel
import com.example.hical.preference.UserPreference
import com.example.hical.response.DeletePayload
import com.example.hical.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RetrofitRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    fun fetchItems() = liveData {
        emit(Result.Loading)
        try {
            val dashboardItemResponse = apiService.fetchItems()
            emit(Result.Success(dashboardItemResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addItems(meals: String, amount: Int) = liveData {
        emit(Result.Loading)
        try {
            val dashboardItemResponse = apiService.addItems(meals, amount)
            emit(Result.Success(dashboardItemResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun analyzeImage(
        image: File
    ) = liveData {
        emit(Result.Loading)
        try {
            val uploadResponse = apiService.analyzeImage(
                MultipartBody.Part.createFormData(
                    "file",
                    image.name,
                    image.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
            )
            emit(Result.Success(uploadResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun deleteItem(deletePayload: DeletePayload) = liveData {
        emit(Result.Loading)
        try {
            val deleteResponse = apiService.deleteItems(deletePayload)
            emit(Result.Success(deleteResponse))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

}