package com.example.tekotest2.service.repository

import com.example.tekotest2.service.response.ColorResponseDTOItem
import com.example.tekotest2.service.response.ProductsResponseDTOItem
import retrofit2.Response

interface ProductRepository {
    suspend fun getProduct() : Response<List<ProductsResponseDTOItem>>
    suspend fun getColor() : Response<List<ColorResponseDTOItem>>
}