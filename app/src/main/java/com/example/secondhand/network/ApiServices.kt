package com.example.secondhand.network

import com.example.secondhand.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    // ENDPOINT: AUTH
    @POST("auth/login")
    fun login(
        @Body requestUser: LoginRequestUser
    ): Call<LoginResponsePostUser>

    @POST("auth/register")
    @Multipart
    fun postRegister(
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("full_name") name: RequestBody?,
        @Part image: MultipartBody.Part,
        @Part("password") password: RequestBody?,
        @Part("phone_number") phone: RequestBody?
    ): Call<RegisterResponsePostUser>

    @PUT("auth/user")
    @Multipart
    fun updateUserProfile(
        @Header("access_token") token: String,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("full_name") fullName: RequestBody?,
        @Part image: MultipartBody.Part,
        @Part("password") password: RequestBody?,
        @Part("phone_number") phoneNumber: RequestBody?
    ): Call<UpdateProfileUserResponse>

    @GET("auth/user")
    fun getSellerData(
        @Header("access_token") token: String
    ): Call<GetSellerResponse>


    // ENDPOINT: BUYER/PRODUCT
    @GET("/buyer/product")
    fun getAllBuyerProduct(): Call<List<GetBuyerProductResponseItem>>




    // ENDPOINT: BUYER/ORDER
    @POST("buyer/order")
    fun updateBidPrice(
        @Header("access_token") token: String,
        @Body reqBidPrice: PostBuyerOrder
    ): Call<PostBuyerOrderResponseItem>


    // ENDPOINT: NOTIFICATION
    @GET("notification")
    fun getAllNotification(@Header("access_token") token: String): Call<List<GetAllNotificationResponseItem>>

    @PATCH("notification/{id}")
    fun updateNotificationStatus(
        @Header("access_token") token: String,
        @Path("id") notificationId: Int,
        @Body notificationStatus: NotificationStatus
    ) : Call<UpdateNotificationStatusResponse>


    //ENDPOINT: SELLER/PRODUCT
    @GET("seller/product")
    fun getSellerProdcut(@Header("access_token") token: String): Call<List<GetSellerProductItem>>

    @POST("seller/product")
    @Multipart
    fun postJualProduct(
        @Header("access_token") token: String,
        @Part("base_price") base_price: RequestBody,
        @Part categories_ids: List<MultipartBody.Part>,
        @Part("description") description: RequestBody,
        @Part fileImage: MultipartBody.Part,
        @Part("location") location: RequestBody,
        @Part("name") name: RequestBody
    ): Call<PostJualProductResponse>

    @DELETE("seller/product/{id}")
    fun deleteProductFromDaftarJualSaya(
        @Header("access_token") token: String,
        @Path("id") id: Int
    ) : Call<GetSellerProductDeleteItemResponse>

    @PUT("seller/product/{id}")
    @Multipart
    fun updateProduct(
        @Header("access_token") token: String,
        @Path("id") id: Int,
        @Part("base_price") basePrice: RequestBody,
        @Part category: List<MultipartBody.Part>,
        @Part("description") description: RequestBody,
        //@Part ImageProduct: MultipartBody.Part,
        @Part("location") location: RequestBody,
        @Part("name") name: RequestBody
    ) : Call<GetSellerProductUpdateResponse>


    // ENDPOINT: SELLER/ORDER
    @PATCH("seller/order/{id}")
    fun updateStatusOrder(
        @Header("access_token") token: String,
        @Path("id") id: Int,
        @Body orderStatus: OrderStatus
    ): Call<Any>

    @GET("seller/order/product/{product_id}")
    fun getInfoSellerOrderProductById(
        @Header("access_token") token: String,
        @Path("product_id") productId: Int
    ) : Call<List<GetSellerOrderProductInfoItem>>
}