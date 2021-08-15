package com.example.tekotest2.service.api.tekoapi

import com.example.tekotest2.model.User
import com.example.tekotest2.service.response.ColorResponseDTOItem
import com.example.tekotest2.service.response.ProductsResponseDTOItem
import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {
    @GET("/api/products")
    suspend fun getProduct(): Response<List<ProductsResponseDTOItem>>
    @GET("/api/colors")
    suspend fun getColors(): Response<List<ColorResponseDTOItem>>
}