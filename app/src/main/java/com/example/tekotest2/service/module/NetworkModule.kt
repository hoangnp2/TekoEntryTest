package com.example.tekotest2.service.module

import android.content.Context
import com.example.tekotest2.BuildConfig
import com.example.tekotest2.service.api.tekoapi.TekoApiService
import com.example.tekotest2.utils.NetworkHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        provideOkHttpClient()
    }
    single {
        getRetrofit(get())
    }
    single {
        getTekoApiService(get())
    }
    single {
        provideNetworkHelper(androidContext())
    }


}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

private fun provideOkHttpClient() : OkHttpClient {
    if(BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        return OkHttpClient.Builder().build()
    }
}

private fun getRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .build()
}

private fun getTekoApiService(retrofit: Retrofit): TekoApiService{
    return retrofit.create(TekoApiService::class.java)
}