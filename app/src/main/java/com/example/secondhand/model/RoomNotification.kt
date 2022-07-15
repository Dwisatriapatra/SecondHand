package com.example.secondhand.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notification")
data class RoomNotification(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val listNotification: List<GetAllNotificationResponseItem>
) : Parcelable