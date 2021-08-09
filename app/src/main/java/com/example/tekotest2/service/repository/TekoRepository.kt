package com.example.tekotest2.service.repository

import com.example.tekotest2.service.api.tekoapi.TekoApiService

public class TekoRepository(private val tekoApiService: TekoApiService) {

    suspend fun getUser() = tekoApiService.getUsers()
}