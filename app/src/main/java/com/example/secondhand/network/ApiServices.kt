package com.example.secondhand.network

import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.model.LoginResponsePostUser
import com.example.secondhand.model.RegisterResponsePostUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {

    @POST("auth/login")
    fun login(@Body requestUser: LoginRequestUser): Call<LoginResponsePostUser>

    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("email") email : String,
        @Field("full_name") full_name : String,
        @Field("password") password : String
    ) : Call<RegisterResponsePostUser>

}