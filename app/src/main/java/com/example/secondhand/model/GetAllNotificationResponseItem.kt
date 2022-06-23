package com.example.secondhand.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetAllNotificationResponseItem(
    val bid_price: Int?,
    val buyer_name: String?,
    val created_at: String?,
    val id: Int?,
    val image_url: String?,
    val product_id: Int?,
    val read: Boolean?,
    val receiver_id: Int?,
    val seller_name: String?,
    val status: String?,
    val transaction_date: String?,
    val updated_at: String?
) : Parcelable