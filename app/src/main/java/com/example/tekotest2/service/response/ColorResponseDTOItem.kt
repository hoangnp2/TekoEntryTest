package com.example.tekotest2.service.response


import com.google.gson.annotations.SerializedName

data class ColorResponseDTOItem(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?
)