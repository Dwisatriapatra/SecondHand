package com.example.secondhand.roomdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.secondhand.model.RoomBuyerProduct

@Dao
interface BuyerProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun roomInsertBuyerProductList(roomBuyerProduct: RoomBuyerProduct)

    @Query("select * from buyer_product")
    fun roomGetAllBuyerProductList(): RoomBuyerProduct
}
