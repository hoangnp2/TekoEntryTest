package com.example.tekotest2.service.repository

import com.example.tekotest2.service.api.tekoapi.ProductApiService

class ProductRepositoryImpl(private val productApiService: ProductApiService) : ProductRepository {
    override suspend fun getProduct() = productApiService.getProduct()
    override suspend fun getColor() = productApiService.getColors()
}