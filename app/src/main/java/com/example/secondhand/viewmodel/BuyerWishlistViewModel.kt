package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetBuyerWishlistResponseItem
import com.example.secondhand.model.PostBuyerWishlistResponse
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BuyerWishlistViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val apiService = api

    private var liveDataBuyerWishlist = MutableLiveData<List<GetBuyerWishlistResponseItem>>()
    val buyerWishlist: LiveData<List<GetBuyerWishlistResponseItem>> = liveDataBuyerWishlist

    private val responseMsg = MutableLiveData<Boolean>()
    val responseMessage: LiveData<Boolean> = responseMsg

    fun getAllBuyerWishlist(token: String) {
        apiService.getAllBuyerWishlist(token)
            .enqueue(object : Callback<List<GetBuyerWishlistResponseItem>> {
                override fun onResponse(
                    call: Call<List<GetBuyerWishlistResponseItem>>,
                    response: Response<List<GetBuyerWishlistResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        liveDataBuyerWishlist.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<List<GetBuyerWishlistResponseItem>>,
                    t: Throwable
                ) {
                    // nothing to do
                }
            })
    }

    fun postBuyerWishlist(token: String, id: RequestBody) {
        apiService.postBuyerWishlist(token, id)
            .enqueue(object : Callback<PostBuyerWishlistResponse> {
                override fun onResponse(
                    call: Call<PostBuyerWishlistResponse>,
                    response: Response<PostBuyerWishlistResponse>
                ) {
                    responseMsg.value = response.isSuccessful
                }

                override fun onFailure(call: Call<PostBuyerWishlistResponse>, t: Throwable) {
                    responseMsg.value = false
                }
            })
    }

    fun deleteBuyerWishlist(token: String, id: Int) {
        apiService.deleteBuyerWishlist(token, id)
            .enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    responseMsg.value = response.isSuccessful
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    responseMsg.value = false
                }
            })
    }
}