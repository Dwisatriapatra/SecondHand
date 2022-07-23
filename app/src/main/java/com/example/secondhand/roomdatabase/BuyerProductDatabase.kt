package com.example.secondhand.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.secondhand.helper.GithubTypeConverters
import com.example.secondhand.model.RoomBuyerProduct

@Database(entities = [RoomBuyerProduct::class], version = 1, exportSchema = false)
@TypeConverters(GithubTypeConverters::class)
abstract class BuyerProductDatabase : RoomDatabase() {

    abstract fun buyerProductDao(): BuyerProductDao

    companion object {
        private var INSTANCE: BuyerProductDatabase? = null


        fun getInstance(context: Context): BuyerProductDatabase? {
            if (INSTANCE == null) {
                synchronized(BuyerProductDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BuyerProductDatabase::class.java,
                        "BuyerProduct"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}