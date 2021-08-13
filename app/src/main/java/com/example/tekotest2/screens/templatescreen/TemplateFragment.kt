package com.example.tekotest2.screens.templatescreen

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tekotest2.R
import com.example.tekotest2.databinding.TemplateLayoutBinding
import com.example.tekotest2.model.User
import com.example.tekotest2.screens.base.BaseFragment
import com.example.tekotest2.screens.templatescreen.adapter.MainAdapter
import com.example.tekotest2.utils.Status
import com.example.tekotest2.viewmodel.TemplateViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class TemplateFragment : BaseFragment<TemplateLayoutBinding>() {
    override val idLayout = R.layout.template_layout
    private val viewModel : TemplateViewModel by viewModel()
    lateinit var adapter : MainAdapter
    override fun initLayout() {
        setupViews()
        viewModel.usersLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    val arr : ArrayList<User> = ArrayList()
                    it.data?.let { it1 -> arr.addAll(it1) }
                    adapter = MainAdapter(arr)
                    binding.recycle.adapter = adapter
                }
                else -> {}
            }
        })
        viewModel.initData()
    }

    private fun setupViews() {
        binding.recycle.layoutManager = LinearLayoutManager(context)
    }
}