package com.example.tekotest2.service
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.tekotest2.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class ServiceBuilder {

    public fun provideOkHttpClient() : OkHttpClient{
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

    public fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
    }
}