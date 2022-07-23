package com.example.secondhand.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.TypeConverter
import com.example.secondhand.model.GetAllNotificationResponseItem
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            true
        } else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
    return false
}

class GithubTypeConverters {
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<GetBuyerProductResponseItem> {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<GetBuyerProductResponseItem?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<GetBuyerProductResponseItem?>?): String {
        return Gson().toJson(someObjects)
    }
}

class NotificatiomTypeConverters {
    @TypeConverter
    fun stringToNotificationObject(string: String?): List<GetAllNotificationResponseItem> {
        if (string.isNullOrEmpty()) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<GetAllNotificationResponseItem?>?>() {}.type
        return Gson().fromJson(string, listType)
    }

    @TypeConverter
    fun notificationObjectToString(notificationList: List<GetAllNotificationResponseItem?>?): String {
        return Gson().toJson(notificationList)
    }
}

