package com.example.tekotest2.utils

import com.example.tekotest2.model.Color
import com.example.tekotest2.model.Product
import com.example.tekotest2.service.response.ColorResponseDTOItem
import com.example.tekotest2.service.response.ProductsResponseDTOItem

class ConvertDataUtils {
    companion object{
        fun convertProduct(productsResponseDTOItem: ProductsResponseDTOItem) : Product{
            val product = Product()
            product.name = productsResponseDTOItem.name
            product.errorDescription = productsResponseDTOItem.errorDescription
            product.sku = productsResponseDTOItem.sku
            product.color  = productsResponseDTOItem.color
            product.image = productsResponseDTOItem.image
            product.id = productsResponseDTOItem.id
            return product
        }

        fun convertColor(colorResponseDTOItem: ColorResponseDTOItem) : Color{
            val color = Color()
            color.id = colorResponseDTOItem.id
            color.name = colorResponseDTOItem.name
            return color
        }
    }
}