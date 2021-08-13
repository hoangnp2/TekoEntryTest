package com.example.tekotest2.service.module

import android.content.Context
import com.example.tekotest2.BuildConfig
import com.example.tekotest2.service.api.tekoapi.TekoApiService
import com.example.tekotest2.service.servicebuilder.ServiceBuilder
import com.example.tekotest2.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        ServiceBuilder.provideOkHttpClient()
    }
    single {
        ServiceBuilder.getRetrofit(get())
    }
    single {
        ServiceBuilder.getTekoApiService(get())
    }
    single {
        ServiceBuilder.provideNetworkHelper(androidContext())
    }


}

