package com.example.secondhand.helper
import com.example.secondhand.model.GetSellerOrderResponseItem

interface PenawaranItemClickListener {
    fun tolakButton(item: GetSellerOrderResponseItem, position: Int)
    fun terimaButton(item: GetSellerOrderResponseItem, position: Int)
    fun statusButton(item: GetSellerOrderResponseItem, position: Int)
    fun hubungiButton(item: GetSellerOrderResponseItem, position: Int)
}