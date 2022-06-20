package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetSellerResponse
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SellerViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val liveDataSeller = MutableLiveData<GetSellerResponse>()
    val seller: LiveData<GetSellerResponse> = liveDataSeller
    private val apiService = api


    fun getSeller(token: String) {
        apiService.getSellerData(token)
            .enqueue(object : Callback<GetSellerResponse> {
                override fun onResponse(
                    call: Call<GetSellerResponse>,
                    response: Response<GetSellerResponse>
                ) {
                    if (response.isSuccessful) {
                        liveDataSeller.value = response.body()
                    } else {
                        //
                    }
                }

                override fun onFailure(call: Call<GetSellerResponse>, t: Throwable) {
                    //
                }

            })
    }

}