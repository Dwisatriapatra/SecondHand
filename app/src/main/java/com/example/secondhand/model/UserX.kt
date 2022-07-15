package com.example.secondhand.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserX(
    val address: String,
    val city: String,
    val email: String,
    val full_name: String,
    val id: Int,
    val image_url: String,
    val phone_number: String
) : Parcelable