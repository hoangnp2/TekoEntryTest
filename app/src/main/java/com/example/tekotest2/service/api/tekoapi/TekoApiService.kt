package com.example.tekotest2.service.api.tekoapi

import com.example.tekotest2.model.User
import retrofit2.Response
import retrofit2.http.GET

interface TekoApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}