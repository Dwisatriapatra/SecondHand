package com.example.secondhand.model

import java.io.File
import java.io.Serializable

data class PostJualProduct(
    val namaBarang: String,
    val harga: Int,
    val lokasi: String,
    val deskripsiProduct: String,
    val imageUri: String,
    val kategoriProduk: String,
    val imageProduct: File
) : Serializable