package com.example.secondhand.network

import com.example.secondhand.model.GetBuyerProductResponseItem
import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.model.LoginResponsePostUser
import com.example.secondhand.model.RegisterRequestUser
import com.example.secondhand.model.RegisterResponsePostUser
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    //patra
    @POST("auth/login")
    fun login(@Body requestUser : LoginRequestUser): Call<LoginResponsePostUser>

    @GET("/buyer/product")
    fun getAllBuyerProduct(@Header("access_token") token: String): Call<List<GetBuyerProductResponseItem>>


    //greta
    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("email") email : String,
        @Field("full_name") full_name : String,
        @Field("password") password : String
    ) : Call<RegisterResponsePostUser>

    @POST("auth/register")
    fun postRegister(@Body reqUser : RegisterRequestUser) : Call<RegisterResponsePostUser>





}