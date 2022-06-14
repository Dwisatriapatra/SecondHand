package com.example.secondhand.network

import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.model.LoginResponsePostUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServices {

    @POST("auth/login")
    fun login(@Body requestUser: LoginRequestUser): Call<LoginResponsePostUser>

}