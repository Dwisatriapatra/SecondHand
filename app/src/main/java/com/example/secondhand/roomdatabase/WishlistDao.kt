package com.example.secondhand.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhand.model.RoomWishlistItem

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(roomWishlistItem: RoomWishlistItem)

    @Query("SELECT * FROM wishlist")
    fun getWishlistData(): List<RoomWishlistItem>
}