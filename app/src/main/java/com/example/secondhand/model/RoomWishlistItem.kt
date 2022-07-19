package com.example.secondhand.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "wishlist")
data class RoomWishlistItem(
    @PrimaryKey(autoGenerate = true) val entitityId: Int?,
    val nameUser: String,
    val name: String,
    val base_price: Int,
    val image_url: String
) : Parcelable