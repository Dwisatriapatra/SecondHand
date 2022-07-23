package com.example.secondhand.model

data class GetSellerProductItem(
    val base_price: Int,
    val Categories: List<CategorySellerProductItem>,
    val created_at: String,
    val description: String,
    val id: Int,
    val image_name: String,
    val image_url: String,
    val location: String,
    val name: String,
    val updated_at: String,
    val user_id: Int,
    val status: String
)