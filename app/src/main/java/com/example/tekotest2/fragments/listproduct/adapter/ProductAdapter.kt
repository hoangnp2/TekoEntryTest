package com.example.tekotest2.fragments.listproduct.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ListPopupWindow
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tekotest2.R
import com.example.tekotest2.databinding.ItemProductLayoutBinding
import com.example.tekotest2.model.Color
import com.example.tekotest2.model.Product
import com.example.tekotest2.utils.ColorUtils
import com.example.tekotest2.utils.GlideHelper

class ProductAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object{
        const val UPDATE_COLOR = 1
        const val UPDATE_ERROR = 2
    }
    private var listProduct : ArrayList<Product> = ArrayList()
    var isOnlyViewMode = false
    var onActionsListener : OnActionsListener? = null
    fun updateListProduct(list : ArrayList<Product>){
        listProduct.clear()
        listProduct.addAll(list)
        notifyDataSetChanged()
    }

    fun addProducts(list : ArrayList<Product>){
        val currentNumbOfProduct = listProduct.size
        listProduct.addAll(list)
        notifyItemRangeInserted(currentNumbOfProduct,list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding : ItemProductLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.item_product_layout,parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isNotEmpty()){
            for(payload in payloads){
                if(payload == UPDATE_COLOR){
                    if(holder is ProductViewHolder){
                        listProduct[position].let {
                            holder.bindDataColor(it)
                        }
                    }
                    return
                }
                if(payload == UPDATE_ERROR){
                    if(holder is ProductViewHolder){
                        listProduct[position].let {
                            holder.bindError(it)
                        }
                    }
                    return
                }
            }
            super.onBindViewHolder(holder, position, payloads)
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ProductViewHolder){
            listProduct[position].let {
                holder.bind(it,isOnlyViewMode,onActionsListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    class ProductViewHolder(private var binding : ItemProductLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        private var inputNameProductWatcher = InputNameProductWatcher()
        private var inputSkuProductWatcher = InputSkuProductWatcher()
        private val arrayAdapter: OptionColorAdapter = OptionColorAdapter(binding.root.context,ColorUtils.arrayListColor)
        private val listenerAdapterColor = object : OptionColorAdapter.OnSelectOptionListener{
            override fun onSelect(option: Color?) {
                option?.let { it ->
                    option.name?.let {strColor ->
                        binding.colorTV.text = strColor
                        binding.savedColorTV.text = strColor
                        listPopupWindow?.dismiss()
                    }
                    product?.color = it.id
                }
            }
        }
        private var onActionsListener : OnActionsListener? = null
        var product : Product? = null
        private var isOnlyViewMode = false

        fun bind(product: Product,viewMode : Boolean,listener : OnActionsListener?){
            this.product = product
            this.isOnlyViewMode = viewMode
            this.onActionsListener = listener
            updateStatusEditProduct(product,binding)
            inputNameProductWatcher.updateData(product)
            inputSkuProductWatcher.updateData(product)
            product.name?.let {
                binding.productNameEdt.text = Editable.Factory.getInstance().newEditable(it)
            }
            binding.productNameEdt.addTextChangedListener(inputNameProductWatcher)
            product.sku?.let {
                binding.skuEdt.text = Editable.Factory.getInstance().newEditable(it)
            }
            binding.skuEdt.addTextChangedListener(inputSkuProductWatcher)
            product.image?.let {
                GlideHelper.loadUrl(it,binding.imageProductIV)
            }
            product.errorDescription?.let {
                binding.productErrorTV.text = it
            }
            bindDataColor(product)

            if(!isOnlyViewMode){
                binding.actionEditProductIV.visibility = View.VISIBLE
                binding.borderColorRL.setOnClickListener { run {
                    if(product.isEditing){
                        arrayAdapter.mOnSelectOptionListener = listenerAdapterColor
                        setUpPopupWindown()
                        listPopupWindow?.show()
                    }
                } }
                binding.actionEditProductIV.setOnClickListener {
                    run {
                        onActionsListener?.let {
                            if(product.isEditing){
                                onActionsListener!!.onClickSaveProduct(product)
                            }else{
                                onActionsListener!!.onClickEditAction(product)
                            }
                        }

                        product.isEditing = !product.isEditing
                        updateStatusEditProduct(product,binding)
                        bindError(product)
                    }
                }
            }else{
                binding.actionEditProductIV.visibility = View.GONE
            }
            bindError(product)

        }

        /*
        * Cập nhật màu sắc
        * */
        fun bindDataColor(product :Product){
            binding.root.context.resources.getString(R.string.no_color).let {
                binding.colorTV.text = it
                binding.savedColorTV.text = it
            }
            if(ColorUtils.arrayListColor.isNotEmpty()){
                product.color?.let {
                    if(ColorUtils.contain(it)){
                        ColorUtils.getColor(it)?.name?.let { nameColor ->
                            binding.colorTV.text = nameColor
                            binding.savedColorTV.text = nameColor
                        }
                    }
                }
            }
        }

        /*
        * Hiển thị lỗi
        * */
        fun bindError(product :Product){
            if(product.validateProduct() != Product.STATUS.INVALID ){
                if (!product.isEditing) {
                    binding.showWarningActionLL.visibility = View.VISIBLE
                } else {
                    binding.showWarningActionLL.visibility = View.GONE
                }
                binding.showWarningActionLL.setOnClickListener {
                    run {
                        onActionsListener?.onShowAlertDialog(
                            binding.root.context.resources.getString(
                                product.validateProduct().idRsContent
                            )
                        )
                    }
                }
            } else {
                binding.showWarningActionLL.visibility = View.GONE
            }
        }
        /*
         * Cập nhật trạng thái sản phẩm sửa/lưu
         * */
        private fun updateStatusEditProduct(product : Product, binding : ItemProductLayoutBinding){
            if(product.isEditing){
                binding.productNameEdt.isEnabled = true
                binding.skuEdt.isEnabled = true
                binding.imageColorExpandIV.visibility = View.VISIBLE
                binding.lineBottomNameProductV.visibility = View.VISIBLE
                binding.lineBottomSkuProductV.visibility = View.VISIBLE
                binding.borderColorRL.visibility = View.VISIBLE
                binding.savedColorTV.visibility = View.GONE
                binding.actionEditProductIV.setImageResource(R.drawable.ic_save)
                binding.productNameEdt.requestFocus()
                product.name?.let {
                    if(it.isNotEmpty() && it.length <= binding.productNameEdt.editableText.length){
                        binding.productNameEdt.setSelection(it.length)
                    }
                }
            }else{
                binding.productNameEdt.setSelection(0)
                binding.productNameEdt.isEnabled = false
                binding.skuEdt.isEnabled = false
                binding.imageColorExpandIV.visibility = View.GONE
                binding.lineBottomNameProductV.visibility = View.GONE
                binding.lineBottomSkuProductV.visibility = View.GONE
                binding.borderColorRL.visibility = View.GONE
                binding.savedColorTV.visibility = View.VISIBLE
                binding.actionEditProductIV.setImageResource(R.drawable.ic_edit_content)
            }
        }
        private var listPopupWindow: ListPopupWindow? = null
        private fun setUpPopupWindown(){
            if (listPopupWindow == null) {
                listPopupWindow = ListPopupWindow(binding.root.context)
            }

            listPopupWindow?.let {popup ->
                popup.height = WindowManager.LayoutParams.WRAP_CONTENT
                popup.width = WindowManager.LayoutParams.WRAP_CONTENT
                popup.isModal = true
                arrayAdapter.let {
                    popup.setAdapter(arrayAdapter)
                }
                popup.anchorView = binding.anchorView
            }
        }
    }

    class InputNameProductWatcher : TextWatcher{
        private var product : Product? = null


        fun updateData(pro : Product) {
            product = pro

        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(editable : Editable?) {
            editable?.let {
                product?.name = editable.toString()
            }
        }
    }

    class InputSkuProductWatcher : TextWatcher{
        private var product : Product? = null
        fun updateData(pro : Product) {
            product = pro
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(editable : Editable?) {
            editable?.let {
                product?.sku = editable.toString()
            }
        }
    }

    interface OnActionsListener{
        fun onClickEditAction(product : Product)
        fun onClickSaveProduct(product : Product)
        fun onShowAlertDialog(content : String)
    }

}