package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.OrderStatus
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SellerOrderViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val responseMsg = MutableLiveData<Boolean>()
    val responseMessage: LiveData<Boolean> = responseMsg
    val apiServices = api

    fun setOrderStatus(token: String, id: Int, orderStatus: OrderStatus){
        apiServices.setStatusOrder(token, id, orderStatus)
            .enqueue(object: Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    responseMsg.value = response.isSuccessful
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    responseMsg.value = false
                }

            })
    }
}