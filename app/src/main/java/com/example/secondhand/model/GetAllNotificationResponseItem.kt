package com.example.secondhand.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllNotificationResponseItem(
    val Product: ProductX?,
    val User: UserX,
    val base_price: String,
    val bid_price: Int,
    val buyer_name: String,
    val createdAt: String,
    val id: Int,
    val image_url: String,
    val notification_type: String,
    val order_id: Int,
    val product_id: Int,
    val product_name: String,
    val read: Boolean,
    val receiver_id: Int,
    val seller_name: String,
    val status: String,
    val transaction_date: String?,
    val updatedAt: String
) : Parcelable