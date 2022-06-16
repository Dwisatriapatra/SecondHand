package com.example.secondhand.model

import com.google.gson.annotations.SerializedName

data class RegisterRequestUser(
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("password")
    val password: String
)
