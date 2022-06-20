package com.example.secondhand.network

import com.example.secondhand.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    //patra
    @POST("auth/login")
    fun login(@Body requestUser: LoginRequestUser): Call<LoginResponsePostUser>

    @GET("/buyer/product")
    fun getAllBuyerProduct(@Header("access_token") token: String): Call<List<GetBuyerProductResponseItem>>

    @GET("notification")
    fun getAllNotification(@Header("access_token") token: String): Call<List<GetAllNotificationResponseItem>>


    //greta
    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("full_name") full_name: String,
        @Field("password") password: String
    ): Call<RegisterResponsePostUser>

    @POST("auth/register")
    fun postRegister(@Body reqUser: RegisterRequestUser): Call<RegisterResponsePostUser>

    @POST("buyer/order")
    fun updateBidPrice(@Header("access_token") token : String,
                        @Body reqBidPrice : PostBuyerOrder) : Call<PostBuyerOrderResponseItem>

//    @PUT("buyer/order/{id}")
//    fun updateBidPrice(
//        @Path("productId") productId: Int,
//        @Body requestOrder : PutBuyerOrder) : Call<List<GetBuyerOrderResponseItem>>
}