package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.PostJualProductResponse
import com.example.secondhand.model.RequestJualProduct
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SellerJualProductViewModel @Inject constructor(api: ApiServices): ViewModel() {
    private val responseMsg = MutableLiveData<Boolean>()
    val responseMessage: LiveData<Boolean> = responseMsg
    val apiServices = api

    fun jualProduct(
        token: String,
        requestJualProduct: RequestJualProduct
    ){
        apiServices.postJualProduct(
            token,
            requestJualProduct.base_price,
            requestJualProduct.categories_ids,
            requestJualProduct.description,
            requestJualProduct.image,
            requestJualProduct.location,
            requestJualProduct.name
        ).enqueue(object: Callback<PostJualProductResponse> {
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