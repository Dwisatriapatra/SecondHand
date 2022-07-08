package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.secondhand.helper.SingleLiveEvent
import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.model.LoginResponsePostUser
import com.example.secondhand.model.RegisterResponsePostUser
import com.example.secondhand.model.UpdateProfileUserResponse
import com.example.secondhand.network.ApiServices
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(api: ApiServices) : ViewModel() {
    private val liveDataUser = MutableLiveData<LoginResponsePostUser>()
    val user: LiveData<LoginResponsePostUser> = liveDataUser

    val responseMessage = SingleLiveEvent<Boolean>()
    private val apiServices = api

    fun userLogin(loginRequestUser: LoginRequestUser) {
        apiServices.login(loginRequestUser)
            .enqueue(object : Callback<LoginResponsePostUser> {

                override fun onResponse(
                    call: Call<LoginResponsePostUser>,
                    response: Response<LoginResponsePostUser>
                ) {
                    if (response.isSuccessful) {
                        responseMessage.value = true
                        liveDataUser.value = response.body()

                    } else {
                        responseMessage.value = false
                    }

                }

                override fun onFailure(call: Call<LoginResponsePostUser>, t: Throwable) {
                    responseMessage.value = false
                }
            })
    }

    fun userRegister(
        email: RequestBody?,
        fullName: RequestBody?,
        password: RequestBody?,
        address: RequestBody?,
        city: RequestBody?,
        phone: RequestBody?,
        //image: MultipartBody.Part
    ) {
        apiServices.postRegister(
            address,
            city,
            email,
            fullName,
            //image,
            password,
            phone
        )
            .enqueue(object : Callback<RegisterResponsePostUser> {
                override fun onResponse(
                    call: Call<RegisterResponsePostUser>,
                    response: Response<RegisterResponsePostUser>
                ) {
                    responseMessage.value = response.isSuccessful
                }

                override fun onFailure(call: Call<RegisterResponsePostUser>, t: Throwable) {
                    responseMessage.value = false

                }
            })
    }

    fun updateDataUser(
        token: String,
        address: RequestBody,
        city: RequestBody,
        email: RequestBody,
        fullName: RequestBody,
        image: MultipartBody.Part,
        password: RequestBody,
        phoneNumber: RequestBody
    ){
        apiServices.updateUserProfile(token,
            address,
            city,
            email,
            fullName,
            image,
            password,
            phoneNumber
        )
            .enqueue(object: Callback<UpdateProfileUserResponse>{
                override fun onResponse(
                    call: Call<UpdateProfileUserResponse>,
                    response: Response<UpdateProfileUserResponse>
                ) {
                    responseMessage.value = response.isSuccessful
                }

                override fun onFailure(call: Call<UpdateProfileUserResponse>, t: Throwable) {
                    responseMessage.value = false
                }
            })
    }
}

