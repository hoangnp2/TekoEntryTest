package com.example.tekotest2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tekotest2.model.Product
import com.example.tekotest2.service.repository.ProductRepositoryImpl
import com.example.tekotest2.utils.ColorUtils
import com.example.tekotest2.utils.ConvertDataUtils
import com.example.tekotest2.utils.NetworkHelper
import com.example.tekotest2.utils.Resource
import kotlinx.coroutines.launch

class TemplateViewModel(
    private var repositoryImpl: ProductRepositoryImpl,
    private var networkHelper: NetworkHelper
) : ViewModel() {
    val requestDataProductLiveData = MutableLiveData<Resource<List<Product>>>()
    val getMoreProductLiveData = MutableLiveData<Resource<List<Product>>>()
    val colorLiveData = MutableLiveData<Resource<Any>>() 
    private val listProductData: ArrayList<Product> = ArrayList()
    val cacheInitialProductData : HashMap<String,Product> = HashMap()
    var enableGetMore = true
    var isAddingProduct = false
    fun initData() {
        getColorsData()
        getProduct(1)
    }

    private fun requestDataProduct() {
        viewModelScope.launch {
            requestDataProductLiveData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                repositoryImpl.getProduct().let {
                    if (it.isSuccessful) {
                        it.body()?.let { list ->
                            listProductData.addAll(list.mapIndexed { _, productsResponseDTOItem ->
                                ConvertDataUtils.convertProduct(productsResponseDTOItem)
                            })
                        }
                        if(listProductData.size >=  PAGE_LIMIT) {
                            requestDataProductLiveData.postValue(Resource.success(listProductData.subList(0,
                                 PAGE_LIMIT
                            )))
                        }else {
                            requestDataProductLiveData.postValue(Resource.success(listProductData))
                            enableGetMore = false
                        }
                    } else requestDataProductLiveData.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else requestDataProductLiveData.postValue(Resource.error("No internet connection", null))
        }
    }

    private fun getMoreProduct(page : Int){
        isAddingProduct = true
        if(listProductData.size >=  PAGE_LIMIT *page){
            getMoreProductLiveData.postValue(Resource.success(listProductData.subList(
                 PAGE_LIMIT * (page-1),
                 PAGE_LIMIT * page )))
        }else{
            if(listProductData.size >  PAGE_LIMIT * (page-1)){
                getMoreProductLiveData.postValue(Resource.success(listProductData.subList( PAGE_LIMIT * (page-1),listProductData.size)))
            }
            enableGetMore = false
        }
    }

    fun getProduct(page : Int){

        if (page < 1 || !enableGetMore) return
        if (page == 1) {
            requestDataProduct()
        } else {
            getMoreProduct(page)
        }
    }

    private fun getColorsData() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repositoryImpl.getColor().let {
                    if (it.isSuccessful) {
                        it.body()?.let { listDTO ->
                            for (color in listDTO.mapIndexed { _, colorDTOItem ->
                                ConvertDataUtils.convertColor(
                                    colorDTOItem
                                )
                            }) {
                                color.id?.let { idColor ->
                                    ColorUtils.listColor.put(idColor.toString(), color)
                                }
                                ColorUtils.arrayListColor.add(color)
                            }
                        }
                        colorLiveData.postValue(Resource.success(null))
                    }
                }
            } else requestDataProductLiveData.postValue(Resource.error("No internet connection", null))
        }
    }

    fun cacheProduct(product: Product){
        if(!product.isChangeData){
            product.id?.let {
                cacheInitialProductData.put(it.toString(), Product(product))
            }
        }
    }

    fun removeCacheProduct(id : String){
        cacheInitialProductData.remove(id)
    }
    companion object {
        const val PAGE_LIMIT = 10
    }
}