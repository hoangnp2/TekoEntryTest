package com.example.tekotest2.fragments.listproduct

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListPopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tekotest2.R
import com.example.tekotest2.databinding.TemplateLayoutBinding
import com.example.tekotest2.model.Product
import com.example.tekotest2.fragments.base.BaseFragment
import com.example.tekotest2.fragments.confirmdialog.ConfirmDialog
import com.example.tekotest2.fragments.listproduct.adapter.ProductAdapter
import com.example.tekotest2.utils.DialogFactory
import com.example.tekotest2.utils.KeyboardUtils
import com.example.tekotest2.utils.Status
import com.example.tekotest2.viewmodel.TemplateViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListProductFragment : BaseFragment<TemplateLayoutBinding>() {
    override val idLayout = R.layout.template_layout
    private val viewModel: TemplateViewModel by viewModel()
    private val adapter: ProductAdapter by lazy { ProductAdapter() }
    private var countPage = 1
    var builder :AlertDialog.Builder? = null
    private val listPopupWindow: ListPopupWindow? = null
    override fun initLayout() {
        setupViews()
        viewModel.requestDataProductLiveData.observe(viewLifecycleOwner, { it ->
            when (it.status) {
                Status.LOADING ->{
                    binding.loadingView.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.loadingView.visibility = View.GONE
                    val arr: ArrayList<Product> = ArrayList()
                    it.data?.let { arr.addAll(it) }
                    adapter.updateListProduct(arr)
                }
                Status.ERROR ->{
                    binding.loadingView.visibility = View.GONE
                    it.message?.let {
                        showAlertDialog(it)
                    }
                }
                else -> {
                }
            }
        })
        viewModel.colorLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    if (adapter.itemCount > 0) {
                        adapter.notifyItemRangeChanged(
                            0,
                            adapter.itemCount - 1,
                            ProductAdapter.UPDATE_COLOR
                        )
                    }
                }
                else -> {
                }
            }
        })
        viewModel.getMoreProductLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val arr: ArrayList<Product> = ArrayList()
                    it.data?.let {listProduct -> arr.addAll(listProduct) }
                    adapter.addProducts(arr)
                    viewModel.isAddingProduct = false
                }
                else -> {
                }
            }
        })
        viewModel.initData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews() {
        builder = AlertDialog.Builder(activity)
        KeyboardUtils.addKeyboardVisibilityListener(binding.root,object : KeyboardUtils.OnKeyboardVisibiltyListener{
            override fun onVisibilityChange(isVisible: Boolean) {
                isVisibleKeyboard = isVisible
            }
        })
        binding.recycle.layoutManager = LinearLayoutManager(context)
        binding.recycle.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!viewModel.enableGetMore || viewModel.isAddingProduct)
                    return
                val layoutManager = recyclerView.layoutManager
                if(layoutManager is LinearLayoutManager){
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    if(totalItemCount < lastVisibleItem + 2){
                        if(viewModel.enableGetMore){
                            countPage++
                            viewModel.getProduct(countPage)
                        }
                    }
                }
            }
        })
        binding.recycle.setOnTouchListener (object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if (enableTouchRcv && isVisibleKeyboard) {
                    hideKeyboard()
                    if (Build.VERSION.SDK_INT >= 28) {
                        try {
                            activity?.currentFocus?.clearFocus()
                        } catch (ignored: Exception) {
                        }
                    }
                    enableTouchRcv = false
                    Handler(Looper.getMainLooper()).postDelayed({ enableTouchRcv = true }, 300)
                }
                return false
            }
        })
        binding.adapter = adapter
        adapter.onActionsListener = object : ProductAdapter.OnActionsListener {
            override fun onClickEditAction(product: Product) {
                if (!product.isChangeData) {
                    viewModel.cacheProduct(product)
                }
            }
            override fun onClickSaveProduct(product: Product) {
                product.id?.toString()?.let {productId ->
                    if(viewModel.cacheInitialProductData.containsKey(productId)){
                        val cacheProduct = viewModel.cacheInitialProductData[productId]
                        cacheProduct?.let {
                            if(product.isSame(cacheProduct)){
                                product.isChangeData = false
                                viewModel.removeCacheProduct(productId)
                            }else{
                                product.isChangeData = true
                            }
                        }
                    }else{
                        product.isChangeData = true
                    }
                }
            }


            override fun onShowAlertDialog(content: String) {
                showAlertDialog(content)
            }
        }

        binding.buttonSubmit.setOnClickListener { kotlin.run {
            showPopupConfirm()
        } }
    }

    private fun showPopupConfirm() {
        val listProductChanged : ArrayList<Product> = ArrayList()
        for(product in viewModel.listProductData){
            if(product.isChangeData && !product.isEditing){
                if(product.validateProduct() != Product.STATUS.INVALID){
                    this.context?.resources?.getString(R.string.general_warning)?.let {
                        showAlertDialog(it)
                    }
                    adapter.notifyItemRangeChanged(
                        0,
                        adapter.itemCount - 1,
                        ProductAdapter.UPDATE_ERROR
                    )
                    return
                }
                listProductChanged.add(product)
            }
        }
        if(listProductChanged.isNotEmpty()){
            val confirmDialog = ConfirmDialog.newInstance(listProductChanged)
            DialogFactory.showDialogFragment(activity,confirmDialog)
        }else{
            this.context?.resources?.getString(R.string.has_no_change)?.let {
                showAlertDialog(it)
            }
        }
    }

    fun showAlertDialog(content: String) {
        builder?.setMessage(content)?.setTitle(R.string.dialog_title)?.setCancelable(false)?.setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, _ -> dialog?.cancel() })
        val alert = builder?.create()
        alert?.show()
    }



    var enableTouchRcv = true
    var isVisibleKeyboard = false
    fun hideKeyboard() {
        if (isVisibleKeyboard) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }
}