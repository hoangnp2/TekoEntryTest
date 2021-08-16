package com.example.tekotest2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tekotest2.R
import com.example.tekotest2.application.App
import com.example.tekotest2.model.Color
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
    val listProductData: ArrayList<Product> = ArrayList()
    val cacheInitialProductData : HashMap<String,Product> = HashMap()
    var enableGetMore = true
    var isAddingProduct = false

    companion object {
        const val PAGE_LIMIT = 10
    }

    fun initData() {
        getColorsData()
        getProduct(1)
    }

    /*
    * Lấy danh sách sản phẩm
    * */
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
            } else requestDataProductLiveData.postValue(Resource.error("Không có kết nối mạng", null))
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

    /*
    * Lấy data màu
    * */
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
                                color.id?.let {
                                    ColorUtils.arrayListColor.add(color)
                                }
                            }
                            ColorUtils.arrayListColor.sortWith(object : Comparator<Color>{
                                override fun compare(color0: Color?, color1: Color?): Int {
                                    color0?.id?.let {id0->
                                        color1?.id?.let {id1->
                                            return id0 - id1
                                        }
                                    }
                                    return 0
                                }
                            })

                        }
                        colorLiveData.postValue(Resource.success(null))
                    }
                }
            } else colorLiveData.postValue(Resource.error("Không có kết nối mạng", null))
        }
    }

    /*
    * Lưu gía trị ban đầu của sản phẩm
    * */
    fun cacheProduct(product: Product){
        if(!product.isChangeData){
            product.id?.let {
                cacheInitialProductData.put(it.toString(), Product(product))
            }
        }
    }

    /*
     * Xoá sản phẩm đã lưu khi đã thay đổi, danh sách cache không lưu sản phẩm đã thay đổi
     * */
    fun removeCacheProduct(id : String){
        cacheInitialProductData.remove(id)
    }

}