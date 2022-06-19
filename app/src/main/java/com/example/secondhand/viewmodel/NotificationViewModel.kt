package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetAllNotificationResponseItem
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val liveDataNotification = MutableLiveData<List<GetAllNotificationResponseItem>>()
    val notification: LiveData<List<GetAllNotificationResponseItem>> = liveDataNotification
    private val apiServices = api
    private val responseMsg = MutableLiveData<Boolean>()
    val responseMessage: LiveData<Boolean> = responseMsg


    fun getAllNotification(token: String) {
        apiServices.getAllNotification(token)
            .enqueue(object : Callback<List<GetAllNotificationResponseItem>> {
                override fun onResponse(
                    call: Call<List<GetAllNotificationResponseItem>>,
                    response: Response<List<GetAllNotificationResponseItem>>
                ) {
                    if (response.isSuccessful) {

                        liveDataNotification.value = response.body()
                        responseMsg.value = true

                    } else {
                        responseMsg.value = false
                    }
                }

                override fun onFailure(
                    call: Call<List<GetAllNotificationResponseItem>>,
                    t: Throwable
                ) {
                    responseMsg.value = false
                }

            })
    }
}