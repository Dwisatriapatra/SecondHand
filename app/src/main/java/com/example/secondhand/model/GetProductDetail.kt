package com.example.secondhand.model

data class GetProductDetail(
    val Categories: List<Category>,
    val User: SellerDetailProduct,
    val base_price: Int,
    val createdAt: String,
    val description: String,
    val id: Int,
    val image_name: String,
    val image_url: String,
    val location: String,
    val name: String,
    val status: String,
    val updatedAt: String,
    val user_id: Int
)