package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.model.LoginResponsePostUser
import com.example.secondhand.model.RegisterResponsePostUser
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(api: ApiServices) : ViewModel(){
    private val liveDataUser = MutableLiveData<LoginResponsePostUser>()
    private val liveDataUserReg = MutableLiveData<RegisterResponsePostUser>()

    val userReg : LiveData<RegisterResponsePostUser> = liveDataUserReg
    val user: LiveData<LoginResponsePostUser> = liveDataUser

    private val liveDataResponseMessage = MutableLiveData<Boolean>()
    val responseMessage : LiveData<Boolean> = liveDataResponseMessage
    private val apiServices = api

    fun userLogin(loginRequestUser: LoginRequestUser){
        apiServices.login(loginRequestUser)
            .enqueue(object: Callback<LoginResponsePostUser> {

                override fun onResponse(
                    call: Call<LoginResponsePostUser>,
                    response: Response<LoginResponsePostUser>
                ) {
                    liveDataResponseMessage.value = response.isSuccessful
                    if(response.isSuccessful){

                        liveDataUser.value = response.body()

                    }else{
                        liveDataResponseMessage.value = false
                    }

                }

                override fun onFailure(call: Call<LoginResponsePostUser>, t: Throwable) {
                    liveDataResponseMessage.value = false
                }
            })
    }

    fun userRegister(email : String, full_name : String, password : String){
        apiServices.register(email, full_name, password)
            .enqueue(object : Callback<RegisterResponsePostUser>{
                override fun onResponse(
                    call: Call<RegisterResponsePostUser>,
                    response: Response<RegisterResponsePostUser>
                ) {
                    if (response.isSuccessful){
                        liveDataUserReg.postValue(response.body())
                    }else{
                        //
                    }
                }

                override fun onFailure(call: Call<RegisterResponsePostUser>, t: Throwable) {
                    //
                }

            })
    }
}