package com.example.secondhand.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.secondhand.helper.notificatiomTypeConverters
import com.example.secondhand.model.RoomNotification

@Database(entities = [RoomNotification::class], version = 1, exportSchema = false)
@TypeConverters(notificatiomTypeConverters::class)
abstract class NotificationDatabase() : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    companion object{
        private var INSTANCE: NotificationDatabase? = null
        fun getInstance(context: Context) : NotificationDatabase?{
            if(INSTANCE == null){
                synchronized(NotificationDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, NotificationDatabase::class.java, "Notification").allowMainThreadQueries().build()

                }
            }
            return INSTANCE
        }
    }
}