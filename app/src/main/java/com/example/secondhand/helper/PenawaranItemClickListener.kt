package com.example.secondhand.helper

import com.example.secondhand.model.GetAllNotificationResponseItem

interface PenawaranItemClickListener {
    fun tolak(item: GetAllNotificationResponseItem, position: Int)
    fun terima(item: GetAllNotificationResponseItem, position: Int)
}