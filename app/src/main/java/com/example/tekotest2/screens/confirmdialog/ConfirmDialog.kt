package com.example.tekotest2.screens.confirmdialog

import com.example.tekotest2.R
import com.example.tekotest2.databinding.ConfirmDialogLayoutBinding
import com.example.tekotest2.model.Product
import com.example.tekotest2.screens.base.BaseDialogFragment
import com.example.tekotest2.screens.templatescreen.adapter.ProductAdapter

class ConfirmDialog : BaseDialogFragment<ConfirmDialogLayoutBinding>() {

    override val idLayout = R.layout.confirm_dialog_layout

    private val adapter: ProductAdapter by lazy { ProductAdapter() }
    private var listProduct : ArrayList<Product> = ArrayList()



    override fun setWidth(): Float {
        return 0.9f
    }

    override fun setHeight(): Float {
        return 0.9f
    }



}