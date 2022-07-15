package com.example.secondhand.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhand.model.RoomBuyerProduct
import com.example.secondhand.model.RoomNotification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun roomInsertNotificationList(roomNotification: RoomNotification)

    @Query("select * from notification")
    fun roomGetAllNotificationList(): RoomNotification
}