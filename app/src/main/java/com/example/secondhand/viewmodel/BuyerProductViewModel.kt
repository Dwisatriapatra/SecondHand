package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.example.secondhand.model.GetProductDetail
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BuyerProductViewModel @Inject constructor(api: ApiServices) : ViewModel() {

    private val liveDataBuyerProduct = MutableLiveData<List<GetBuyerProductResponseItem>>()
    val buyerProduct: LiveData<List<GetBuyerProductResponseItem>> = liveDataBuyerProduct
    private val apiServices = api

    private val liveDataBuyerProductById = MutableLiveData<GetProductDetail>()
    val buyerProductById : LiveData<GetProductDetail> = liveDataBuyerProductById

    fun getAllBuyerProduct() {
        apiServices.getAllBuyerProduct()
            .enqueue(object : Callback<List<GetBuyerProductResponseItem>> {
                override fun onResponse(
                    call: Call<List<GetBuyerProductResponseItem>>,
                    response: Response<List<GetBuyerProductResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        liveDataBuyerProduct.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<List<GetBuyerProductResponseItem>>,
                    t: Throwable
                ) {
                    //
                }

            })
    }
    fun getBuyerProductById(idProduct: Int){
        apiServices.getBuyerProductById(idProduct)
            .enqueue(object: Callback<GetProductDetail>{
                override fun onResponse(
                    call: Call<GetProductDetail>,
                    response: Response<GetProductDetail>
                ) {
                    if(response.isSuccessful){
                        liveDataBuyerProductById.value = response.body()
                    }
                }

                override fun onFailure(call: Call<GetProductDetail>, t: Throwable) {
                    //
                }

            })
    }

}