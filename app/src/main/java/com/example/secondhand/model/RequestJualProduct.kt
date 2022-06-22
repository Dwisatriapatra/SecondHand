package com.example.secondhand.model

import com.google.gson.annotations.SerializedName

data class RequestJualProduct(
    @SerializedName("base_price")
    val base_price: Int,
    @SerializedName("categories_ids")
    val categories_ids: List<Int>,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("location")
    val location: String,
    @SerializedName("name")
    val name: String
)
