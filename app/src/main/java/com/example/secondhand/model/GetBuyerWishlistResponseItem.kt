package com.example.secondhand.model

data class GetBuyerWishlistResponseItem(
    val Product: Product,
    val id: Int,
    val product_id: Int,
    val user_id: Int
)