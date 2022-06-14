package com.example.secondhand.model

import com.google.gson.annotations.SerializedName

data class LoginRequestUser(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)