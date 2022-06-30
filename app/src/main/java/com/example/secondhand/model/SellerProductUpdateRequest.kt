package com.example.secondhand.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class SellerProductUpdateRequest(
    val basePrice: RequestBody?,
    val category: RequestBody?,
    val description: RequestBody?,
    val imageProduct: MultipartBody.Part,
    val location: RequestBody?
)