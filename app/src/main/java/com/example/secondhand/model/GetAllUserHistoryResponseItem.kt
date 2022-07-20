package com.example.secondhand.model

data class GetAllUserHistoryResponseItem(
    val category: String,
    val id: Int,
    val image_url: String,
    val price: Int,
    val product_name: String,
    val status: String,
    val transaction_date: String,
    val user_id: Int
)