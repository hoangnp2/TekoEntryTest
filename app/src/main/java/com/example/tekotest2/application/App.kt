package com.example.tekotest2.application

import android.app.Application
import com.example.tekotest2.service.module.networkModule
import com.example.tekotest2.service.module.repositoryModule
import com.example.tekotest2.service.module.viewModelModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}
