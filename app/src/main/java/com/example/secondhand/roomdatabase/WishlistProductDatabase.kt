package com.example.secondhand.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.secondhand.model.RoomWishlistItem

@Database(entities = [RoomWishlistItem::class], version = 1, exportSchema = false)
abstract class WishlistProductDatabase : RoomDatabase() {
    abstract fun wishlistDao(): WishlistDao
    companion object{
        private var INSTANCE: WishlistProductDatabase? = null
        fun getInstance(context: Context): WishlistProductDatabase{
            if(INSTANCE == null){
                synchronized(WishlistProductDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        WishlistProductDatabase::class.java,
                        "WishlistProduct"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE!!
        }
    }
}