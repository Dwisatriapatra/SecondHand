package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetSellerProductDeleteItemResponse
import com.example.secondhand.model.GetSellerProductItem
import com.example.secondhand.model.GetSellerProductUpdateResponse
import com.example.secondhand.model.SellerProductUpdateRequest
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SellerProductViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val liveDataSellerProduct = MutableLiveData<List<GetSellerProductItem>>()
    val sellerProduct: LiveData<List<GetSellerProductItem>> = liveDataSellerProduct
    val apiServices = api

    private val liveDataResponseMsg = MutableLiveData<Boolean>()
    val responseMessage: LiveData<Boolean> = liveDataResponseMsg

    fun getAllSellerProduct(token: String) {
        apiServices.getSellerProdcut(token)
            .enqueue(object : Callback<List<GetSellerProductItem>> {
                override fun onResponse(
                    call: Call<List<GetSellerProductItem>>,
                    response: Response<List<GetSellerProductItem>>
                ) {
                    if (response.isSuccessful) {
                        liveDataSellerProduct.value = response.body()
                    }
                }

                override fun onFailure(call: Call<List<GetSellerProductItem>>, t: Throwable) {
                    //
                }

            })
    }

    fun deleteProductFromDaftarJualSaya(token: String, id: Int){
        apiServices.deleteProductFromDaftarJualSaya(token, id)
            .enqueue(object: Callback<GetSellerProductDeleteItemResponse>{
                override fun onResponse(
                    call: Call<GetSellerProductDeleteItemResponse>,
                    response: Response<GetSellerProductDeleteItemResponse>
                ) {
                    //
                }

                override fun onFailure(
                    call: Call<GetSellerProductDeleteItemResponse>,
                    t: Throwable
                ) {
                    //
                }

            })
    }

    fun updateProductInDaftarJualSaya(token: String, id: Int, sellerProductUpdateRequest: SellerProductUpdateRequest){
        apiServices.updateProduct(
            token,
            id,
            sellerProductUpdateRequest.basePrice!!,
            sellerProductUpdateRequest.category,
            sellerProductUpdateRequest.description!!,
            //sellerProductUpdateRequest.imageProduct,
            sellerProductUpdateRequest.location!!,
            sellerProductUpdateRequest.name
        ).enqueue(object: Callback<GetSellerProductUpdateResponse>{
            override fun onResponse(
                call: Call<GetSellerProductUpdateResponse>,
                response: Response<GetSellerProductUpdateResponse>
            ) {
                liveDataResponseMsg.value = response.isSuccessful
            }

            override fun onFailure(call: Call<GetSellerProductUpdateResponse>, t: Throwable) {
                liveDataResponseMsg.value = false
            }

        })
    }
}