package com.example.tekotest2.service.servicebuilder

import android.content.Context
import com.example.tekotest2.BuildConfig
import com.example.tekotest2.service.api.tekoapi.ProductApiService
import com.example.tekotest2.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceBuilder {
    companion object {
        fun provideNetworkHelper(context: Context) = NetworkHelper(context)

        fun provideOkHttpClient(): OkHttpClient {
            return if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            } else {
                OkHttpClient.Builder().build()
            }
        }

        fun getRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .build()
        }

        fun getProductsApiService(retrofit: Retrofit): ProductApiService {
            return retrofit.create(ProductApiService::class.java)
        }
    }

}