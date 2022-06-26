package com.example.secondhand.helper

import com.example.secondhand.model.GetSellerProductItem

interface DaftarJualProductSayaItemClickListener {

    fun editProductInDaftarJualSaya(item: GetSellerProductItem, position: Int)
    fun deleteProductFromDaftarJualSaya(item: GetSellerProductItem, position: Int)

}