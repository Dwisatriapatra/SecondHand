package com.example.secondhand.model

data class GetSellerOrderResponseItem(
    val Product: ProductSellerOrder,
    val User: UserSellerOrder,
    val base_price: Int,
    val buyer_id: Int,
    val createdAt: String,
    val id: Int,
    val image_product: String,
    val price: Int,
    val product_id: Int,
    val product_name: String,
    val status: String,
    val transaction_date: String,
    val updatedAt: String
)