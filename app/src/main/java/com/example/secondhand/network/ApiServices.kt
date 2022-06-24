package com.example.secondhand.network

import com.example.secondhand.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    //patra
    @POST("auth/login")
    fun login(@Body requestUser: LoginRequestUser): Call<LoginResponsePostUser>

    @GET("/buyer/product")
    fun getAllBuyerProduct(): Call<List<GetBuyerProductResponseItem>>

    @GET("notification")
    fun getAllNotification(@Header("access_token") token: String): Call<List<GetAllNotificationResponseItem>>

    @GET("auth/user")
    fun getSellerData(@Header("access_token") token: String): Call<GetSellerResponse>

    @GET("seller/product")
    fun getSellerProdcut(@Header("access_token") token: String): Call<List<GetSellerProductItem>>

    @POST("seller/product")
    @Multipart
    fun postJualProduct(
        @Header("access_token") token: String,
        @Part("base_price") base_price: Int,
        @Part("categories_ids") categories_ids: List<Int>,
        @Part("description") description: String,
        @Part("image") image: String?,
        @Part("location") location: String,
        @Part("name") name: String
    ): Call<PostJualProductResponse>

    @PATCH("seller/order/{id}")
    fun setStatusOrder(
        @Header("access_token") token: String,
        @Path("id") id: Int,
        @Body orderStatus: OrderStatus
    ): Call<Any>

    //greta
//    @POST("auth/register")
//    @FormUrlEncoded
//    fun register(
//        @Field("email") email: String,
//        @Field("full_name") full_name: String,
//        @Field("password") password: String
//    ): Call<RegisterResponsePostUser>

    @POST("auth/register")
    fun postRegister(@Body reqUser: RegisterRequestUser): Call<RegisterResponsePostUser>

    //    fitur tawar menawar
    @POST("buyer/order")
    fun updateBidPrice(
        @Header("access_token") token: String,
        @Body reqBidPrice: PostBuyerOrder
    ): Call<PostBuyerOrderResponseItem>

//    @PUT("buyer/order/{id}")
//    fun updateBidPrice(
//        @Path("productId") productId: Int,
//        @Body requestOrder : PutBuyerOrder) : Call<List<GetBuyerOrderResponseItem>>
}