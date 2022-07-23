package com.example.secondhand.model

data class UpdateStatusProductResponse(
    val base_price: Int,
    val created_at: String,
    val description: String,
    val id: Int,
    val image_name: String,
    val image_url: String,
    val location: String,
    val name: String,
    val updated_at: String,
    val user_id: Int
)