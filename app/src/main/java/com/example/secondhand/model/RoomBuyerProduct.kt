package com.example.secondhand.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.secondhand.helper.GithubTypeConverters
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "buyer_product")
data class RoomBuyerProduct(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    //@TypeConverters(GithubTypeConverters::class)
    val listBuyerProduct: List<GetBuyerProductResponseItem>?
) : Parcelable
