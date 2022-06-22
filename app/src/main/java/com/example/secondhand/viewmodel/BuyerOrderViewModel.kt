package com.example.secondhand.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.PostBuyerOrder
import com.example.secondhand.model.PostBuyerOrderResponseItem
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BuyerOrderViewModel @Inject constructor(api : ApiServices) : ViewModel() {

    private val liveDataBuyerOrder = MutableLiveData<PostBuyerOrderResponseItem>()
    private val apiServices = api

    fun postBuyerOrder(token : String, postBuyerOrder : PostBuyerOrder){
        apiServices.updateBidPrice(token, postBuyerOrder)
            .enqueue(object : Callback<PostBuyerOrderResponseItem>{
                override fun onResponse(
                    call: Call<PostBuyerOrderResponseItem>,
                    response: Response<PostBuyerOrderResponseItem>
                ) {
                    if (response.isSuccessful){
                        liveDataBuyerOrder.value = response.body()
                    }
                }

                override fun onFailure(call: Call<PostBuyerOrderResponseItem>, t: Throwable) {

                }

            })
    }


}