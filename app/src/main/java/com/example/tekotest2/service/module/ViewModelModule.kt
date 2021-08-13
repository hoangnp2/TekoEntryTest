package com.example.tekotest2.service.module

import com.example.tekotest2.viewmodel.TemplateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TemplateViewModel(repository = get(),networkHelper = get()) }
}