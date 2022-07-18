package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetSellerBannerItem
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BannerViewModel @Inject constructor(api : ApiServices) : ViewModel() {

    private val liveDataBanner = MutableLiveData<List<GetSellerBannerItem>>()
    val banner : LiveData<List<GetSellerBannerItem>> = liveDataBanner

    private val apiServices = api

    fun getAllBanner(){
        apiServices.getImageBanner()
            .enqueue(object : Callback<List<GetSellerBannerItem>>{
                override fun onResponse(
                    call: Call<List<GetSellerBannerItem>>,
                    response: Response<List<GetSellerBannerItem>>
                ) {
                    if (response.isSuccessful){
                        liveDataBanner.value = response.body()
                    } else{
                        //

                    }

                }

                override fun onFailure(call: Call<List<GetSellerBannerItem>>, t: Throwable) {
                    //
                }

            })
    }
}