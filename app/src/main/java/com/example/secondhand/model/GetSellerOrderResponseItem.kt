package com.example.secondhand.model

data class GetSellerOrderResponseItem(
    val Product: ProductSellerOrder,
    val User: UserSellerOrder,
    val buyer_id: Int,
    val createdAt: String,
    val id: Int,
    val price: Int,
    val product_id: Int,
    val status: String,
    val updatedAt: String
)