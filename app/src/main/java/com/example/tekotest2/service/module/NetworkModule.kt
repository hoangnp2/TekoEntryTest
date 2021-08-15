package com.example.tekotest2.service.module

import com.example.tekotest2.service.servicebuilder.ServiceBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single {
        ServiceBuilder.provideOkHttpClient()
    }
    single {
        ServiceBuilder.getRetrofit(get())
    }
    single {
        ServiceBuilder.getProductsApiService(get())
    }
    single {
        ServiceBuilder.provideNetworkHelper(androidContext())
    }


}

