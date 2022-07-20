package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.GetAllUserHistoryResponseItem
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val liveDataHistory = MutableLiveData<List<GetAllUserHistoryResponseItem>>()
    val history : LiveData<List<GetAllUserHistoryResponseItem>> = liveDataHistory
    val apiServices = api
    fun getAllUserHistory(token: String){
        apiServices.getAllUserHistory(token)
            .enqueue(object: Callback<List<GetAllUserHistoryResponseItem>> {
                override fun onResponse(
                    call: Call<List<GetAllUserHistoryResponseItem>>,
                    response: Response<List<GetAllUserHistoryResponseItem>>
                ) {
                    if(response.isSuccessful){
                        liveDataHistory.value = response.body()
                    }
                }

                override fun onFailure(
                    call: Call<List<GetAllUserHistoryResponseItem>>,
                    t: Throwable
                ) {
                    // nothing to do
                }

            })
    }
}