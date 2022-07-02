package com.example.secondhand.model

data class ProductSellerOrder(
    val base_price: Int,
    val description: String,
    val image_name: String,
    val image_url: String,
    val location: String,
    val name: String,
    val status: String,
    val user_id: Int
)