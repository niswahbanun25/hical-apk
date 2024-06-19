package com.example.hical.api

import com.example.hical.response.AddItemResponse
import com.example.hical.response.AnalyzeResponse
import com.example.hical.response.DashboardItemResponse
import com.example.hical.response.DeletePayload
import com.example.hical.response.DeleteResponse
import com.example.hical.response.LoginResponse
import com.example.hical.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("/api/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("/api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("/api/items")
    suspend fun fetchItems(): DashboardItemResponse

    @FormUrlEncoded
    @POST("/api/items")
    suspend fun addItems(
        @Field("meal") meal: String,
        @Field("amount") amount: Int
    ): AddItemResponse

    @Multipart
    @POST("/api/predict")
    suspend fun analyzeImage(
        @Part file: MultipartBody.Part,
    ): AnalyzeResponse

    @HTTP(method = "DELETE", path = "/api/items", hasBody = true)
    suspend fun deleteItems(
        @Body deletePayload: DeletePayload,
    ): DeleteResponse
//    @FormUrlEncoded
//    @DELETE("/api/items")
//    suspend fun deleteItems(
//        @Field("id") id: String,
//    ): DeleteResponse
}