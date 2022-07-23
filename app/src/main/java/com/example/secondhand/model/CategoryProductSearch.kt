package com.example.secondhand.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryProductSearch(
    val id: Int,
    val name: String
) : Parcelable