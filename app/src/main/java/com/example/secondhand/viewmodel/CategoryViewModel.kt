package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.MainProductCategoryResponseItem
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(apiServices: ApiServices) : ViewModel() {
    private var liveDataCategory = MutableLiveData<List<MainProductCategoryResponseItem>>()
    val listCategory: LiveData<List<MainProductCategoryResponseItem>> = liveDataCategory
    val api = apiServices


    fun getAllProductCategoryAvailable() {
        api.getAllProductCategory()
            .enqueue(object : Callback<List<MainProductCategoryResponseItem>> {
                override fun onResponse(
                    call: Call<List<MainProductCategoryResponseItem>>,
                    response: Response<List<MainProductCategoryResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        liveDataCategory.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<List<MainProductCategoryResponseItem>>,
                    t: Throwable
                ) {
                    //nothing to do
                }

            })
    }
}