package com.example.tekotest2.screens.templatescreen

import com.example.tekotest2.R
import com.example.tekotest2.databinding.TemplateLayoutBinding
import com.example.tekotest2.screens.base.BaseFragment
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

public class TemplateFragment : BaseFragment<TemplateLayoutBinding>() {
    override val idLayout = R.layout.template_layout
    val viewModel : TemplateViewModel  by viewModel()

    override fun initLayout() {


    }
}