package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetSellerOrderProductInfoItem
import com.example.secondhand.model.GetSellerOrderResponseItem
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
    private val liveDataSellerProductInfo = MutableLiveData<GetSellerOrderProductInfoItem>()
    val sellerProductInfo: LiveData<GetSellerOrderProductInfoItem> = liveDataSellerProductInfo

    private val liveDataSellerOrder = MutableLiveData<List<GetSellerOrderResponseItem>>()
    val sellerOrder: LiveData<List<GetSellerOrderResponseItem>> = liveDataSellerOrder

    fun getSellerOrderProductInfo(token: String, productId: Int, buyerName: String){
        apiServices.getInfoSellerOrderProductById(token, productId)
            .enqueue(object: Callback<List<GetSellerOrderProductInfoItem>>{
                override fun onResponse(
                    call: Call<List<GetSellerOrderProductInfoItem>>,
                    response: Response<List<GetSellerOrderProductInfoItem>>
                ) {
                    responseMsg.value = response.isSuccessful
                    if(response.isSuccessful){
                        for(i in response.body()!!.indices){
                            if(response.body()!![i].User.full_name == buyerName){
                                liveDataSellerProductInfo.value = response.body()!![i]
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<List<GetSellerOrderProductInfoItem>>,
                    t: Throwable
                ) {
                    responseMsg.value = false
                }

            })
    }


    fun updateOrderStatus(token: String, id: Int, orderStatus: OrderStatus) {
        apiServices.updateStatusOrder(token, id, orderStatus)
            .enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    responseMsg.value = response.isSuccessful
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    responseMsg.value = false
                }

            })
    }

    fun getAllSellerOrder(token: String){
        apiServices.getAllSellerOrder(token)
            .enqueue(object: Callback<List<GetSellerOrderResponseItem>>{
                override fun onResponse(
                    call: Call<List<GetSellerOrderResponseItem>>,
                    response: Response<List<GetSellerOrderResponseItem>>
                ) {
                    if(response.isSuccessful){
                        liveDataSellerOrder.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<GetSellerOrderResponseItem>>, t: Throwable) {
                    //
                }

            })
    }
}