package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.PostJualProductResponse
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SellerJualProductViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val responseMsg = MutableLiveData<Boolean>()
    val responseMessage: LiveData<Boolean> = responseMsg
    val apiServices = api

    fun jualProduct(
        token: String,
        basePrice: RequestBody,
        categories_ids: List<MultipartBody.Part>,
        description: RequestBody,
        image: MultipartBody.Part,
        location: RequestBody,
        name: RequestBody
    ) {
        apiServices.postJualProduct(
            token,
            basePrice,
            categories_ids,
            description,
            image,
            location,
            name
        ).enqueue(object : Callback<PostJualProductResponse> {
            override fun onResponse(
                call: Call<PostJualProductResponse>,
                response: Response<PostJualProductResponse>
            ) {
                responseMsg.value = response.isSuccessful
            }

            override fun onFailure(call: Call<PostJualProductResponse>, t: Throwable) {
                responseMsg.value = false
            }

        })
    }
}