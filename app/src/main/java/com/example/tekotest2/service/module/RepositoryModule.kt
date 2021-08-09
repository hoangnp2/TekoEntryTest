package com.example.tekotest2.service.module

import com.example.tekotest2.service.repository.TekoRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        TekoRepository(get())
    }

}