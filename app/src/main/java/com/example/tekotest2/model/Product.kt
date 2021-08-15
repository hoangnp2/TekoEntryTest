package com.example.tekotest2.model

import com.example.tekotest2.R

class Product() {
    enum class STATUS(var idRsContent : Int){
        NAME_EMPTY(R.string.name_empty_warning),
        NAME_MAX_SIZE(R.string.name_too_long_warning),
        SKU_EMPTY(R.string.sku_empty_warning),
        SKU_MAX_SIZE(R.string.sku_too_long_warning),
        INVALID(R.string.invalid),
    }

    var color: Int? = 0
    var errorDescription: String? = ""
    var id: Int? = 0
    var image: String? = ""
    var name: String? = ""
    var sku: String? = ""
    var isEditing : Boolean = false
    var isChangeData : Boolean = false

    constructor(otherProduct : Product) : this() {
        this.color = otherProduct.color
        this.errorDescription = otherProduct.errorDescription
        this.id = otherProduct.id
        this.sku = otherProduct.sku
        this.image = otherProduct.image
        this.name = otherProduct.name
        this.isEditing = otherProduct.isEditing
        this.isChangeData = otherProduct.isChangeData
    }

    fun isSame(otherProduct : Product) : Boolean{
        return this.name == otherProduct.name && this.sku == otherProduct.sku && this.color == otherProduct.color
    }

    fun validateProduct(): STATUS {
        if (isEmpty(this.name)) {
            return STATUS.NAME_EMPTY
        }
        this.name?.let {
            if (it.length > 50) {
                return STATUS.NAME_MAX_SIZE
            }

        }
        if (isEmpty(this.sku)) {
            return STATUS.SKU_EMPTY
        }
        this.sku?.let {
            if (it.length > 20) {
                return STATUS.SKU_MAX_SIZE
            }

        }
        return STATUS.INVALID
    }

    private fun isEmpty(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' }.isEmpty()
    }
}