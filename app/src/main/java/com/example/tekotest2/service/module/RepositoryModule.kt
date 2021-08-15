package com.example.tekotest2.service.module

import com.example.tekotest2.service.repository.ProductRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single {
        ProductRepositoryImpl(get())
    }

}