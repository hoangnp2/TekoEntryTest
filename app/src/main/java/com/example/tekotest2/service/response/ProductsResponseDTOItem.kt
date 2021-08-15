package com.example.tekotest2.service.response


import com.google.gson.annotations.SerializedName

data class ProductsResponseDTOItem(
    @SerializedName("color")
    var color: Int?,
    @SerializedName("errorDescription")
    var errorDescription: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("sku")
    var sku: String?
)