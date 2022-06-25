package com.example.secondhand.helper

import com.example.secondhand.model.GetAllNotificationResponseItem

interface NotificationItemClickListener {
    fun clickOnNotificationReadStatus(item: GetAllNotificationResponseItem, position: Int)
    fun clickOnNotificationBodySection(item: GetAllNotificationResponseItem, position: Int)
}