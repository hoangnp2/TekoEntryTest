package com.example.tekotest2.fragments.confirmdialog

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tekotest2.R
import com.example.tekotest2.databinding.ConfirmDialogLayoutBinding
import com.example.tekotest2.model.Product
import com.example.tekotest2.fragments.base.BaseDialogFragment
import com.example.tekotest2.fragments.listproduct.adapter.ProductAdapter

class ConfirmDialog : BaseDialogFragment<ConfirmDialogLayoutBinding>() {

    override val idLayout = R.layout.confirm_dialog_layout

    private val adapter: ProductAdapter by lazy { ProductAdapter() }
    private var listProduct : ArrayList<Product> = ArrayList()

    companion object{
        fun newInstance(list : ArrayList<Product>) : ConfirmDialog {
            val confirmDialog = ConfirmDialog()
            confirmDialog.listProduct = list
            return confirmDialog
        }
    }

    override fun setWidth(): Float {
        return 0.9f
    }

    override fun setHeight(): Float {
        return 0.7f
    }

    override fun initLayout() {
        binding.adapter = adapter
        adapter.isOnlyViewMode = true
        binding.listProduct.layoutManager = LinearLayoutManager(context)
        binding.listProduct.adapter = adapter
        adapter.updateListProduct(listProduct)
        binding.actionConfirm.setOnClickListener { kotlin.run {
            dismissAllowingStateLoss()
        } }
    }
}