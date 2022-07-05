package com.example.secondhand.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryProducrSearch(
    val id: Int,
    val name: String
) : Parcelable