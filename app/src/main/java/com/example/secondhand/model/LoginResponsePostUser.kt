package com.example.secondhand.model

import com.google.gson.annotations.SerializedName

data class LoginResponsePostUser(
    @SerializedName("access_token")
    val access_token: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)