package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetSellerProductDeleteItemResponse
import com.example.secondhand.model.GetSellerProductItem
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class SellerProductViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val liveDataSellerProduct = MutableLiveData<List<GetSellerProductItem>>()
    val sellerProduct: LiveData<List<GetSellerProductItem>> = liveDataSellerProduct
    val apiServices = api

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
}