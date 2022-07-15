package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondhand.model.RoomNotification
import com.example.secondhand.roomdatabase.NotificationDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomNotificationViewModel @Inject constructor(notificationDao: NotificationDao) : ViewModel() {
    private val liveDataRoomNotification = MutableLiveData<RoomNotification>()
    val roomNotification : LiveData<RoomNotification> = liveDataRoomNotification

    private val dao = notificationDao
    init {
        viewModelScope.launch {
            val dataBuyerProduct = notificationDao.roomGetAllNotificationList()
            liveDataRoomNotification.value = dataBuyerProduct
        }
    }

    fun insertNotificationList(list: RoomNotification){
        viewModelScope.launch {
            dao.roomInsertNotificationList(list)
        }
    }
}