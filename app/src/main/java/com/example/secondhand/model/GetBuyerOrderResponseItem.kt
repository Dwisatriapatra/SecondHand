package com.example.secondhand.model


import com.google.gson.annotations.SerializedName

data class GetBuyerOrderResponseItem(
    @SerializedName("buyer_id")
    val buyerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("Product")
    val product: GetBuyerProductResponseItem,
    @SerializedName("productId")
    val productId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("User")
    val user: RegisterResponsePostUser
)