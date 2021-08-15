package com.example.tekotest2.screens.templatescreen.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
    private var listProduct : ArrayList<Product> = ArrayList()
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
            }
            super.onBindViewHolder(holder, position, payloads)
        }else{
            super.onBindViewHolder(holder, position, payloads)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ProductViewHolder){
            listProduct[position].let {
                holder.setData(onActionsListener)
                holder.bind(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    class ProductViewHolder(private var binding : ItemProductLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        private var inputNameProductWatcher = InputNameProductWatcher()
        private var inputSkuProductWatcher = InputSkuProductWatcher()
        private val arrayAdapter: OptionColorAdapter = OptionColorAdapter(ColorUtils.arrayListColor)
        private val listenerAdapterColor = object : OptionColorAdapter.OnSelectOptionListener{
            override fun onSelect(option: Color?) {
                option?.let { it ->
                    option.name?.let {strColor ->
                        binding.colorTV.text = strColor
                        onActionsListener?.onSelectItem()

                    }
                    product?.color = it.id
                }
            }
        }
        var onActionsListener : OnActionsListener? = null
        var product : Product? = null
        fun setData(onActionsListener : OnActionsListener?){

            this.onActionsListener = onActionsListener
        }

        fun bind(product: Product){
            this.product = product
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
            binding.colorTV.text = "--"

            if(ColorUtils.listColor.isNotEmpty()){
                product.color?.toString().let {
                    if(ColorUtils.listColor.containsKey(it)){
                        ColorUtils.listColor[it]?.name?.let { nameColor ->
                            binding.colorTV.text = nameColor
                        }
                    }
                }
            }
            binding.borderColorRL.setOnClickListener { run {
                arrayAdapter.mOnSelectOptionListener = listenerAdapterColor
                binding.colorSpinner.adapter = arrayAdapter
                binding.colorSpinner.performClick()
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
                }
            }
        }
        fun bindDataColor(product :Product){
            binding.colorTV.text = "--"
            if(ColorUtils.listColor.isNotEmpty()){
                product.color?.toString().let {
                    if(ColorUtils.listColor.containsKey(it)){
                        ColorUtils.listColor[it]?.name?.let { nameColor ->
                            binding.colorTV.text = nameColor
                        }
                    }
                }
            }
        }
        private fun updateStatusEditProduct(product : Product, binding : ItemProductLayoutBinding){
            if(product.isEditing){
                binding.productNameEdt.isEnabled = true
                binding.skuEdt.isEnabled = true
                binding.imageColorExpandIV.visibility = View.VISIBLE
                binding.lineBottomNameProductV.visibility = View.VISIBLE
                binding.lineBottomSkuProductV.visibility = View.VISIBLE
                binding.borderColorRL.setBackgroundResource(R.drawable.bg_gray_border_nostroke_radius_5dp)
                binding.actionEditProductIV.setImageResource(R.drawable.ic_save)
                binding.productNameEdt.requestFocus()
            }else{
                binding.productNameEdt.setSelection(0)
                binding.productNameEdt.isEnabled = false
                binding.skuEdt.isEnabled = false
                binding.imageColorExpandIV.visibility = View.GONE
                binding.lineBottomNameProductV.visibility = View.GONE
                binding.lineBottomSkuProductV.visibility = View.GONE
                binding.borderColorRL.setBackgroundResource(R.drawable.bg_white_round_radius_0dp)
                binding.actionEditProductIV.setImageResource(R.drawable.ic_edit_content)
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
        fun onSelectItem()
    }

}