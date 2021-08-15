package com.example.tekotest2.model

class Product() {
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
        this.image = otherProduct.image
        this.name = otherProduct.name
        this.isEditing = otherProduct.isEditing
        this.isChangeData = otherProduct.isChangeData
    }

    fun isSame(otherProduct : Product) : Boolean{
        return this.name == otherProduct.name && this.sku == otherProduct.sku && this.color == otherProduct.color
    }

}