package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondhand.model.RoomWishlistItem
import com.example.secondhand.roomdatabase.WishlistDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomWishlistProductViewModel @Inject constructor(wishlistDao: WishlistDao) : ViewModel() {
    private val liveDataRoomWishlistProduct = MutableLiveData<List<RoomWishlistItem>>()
    val roomWishlistProduct : LiveData<List<RoomWishlistItem>> = liveDataRoomWishlistProduct

    private val dao = wishlistDao
    init {
        viewModelScope.launch {
            val dataWishlist = wishlistDao.getWishlistData()
            liveDataRoomWishlistProduct.value = dataWishlist
        }
    }

    fun insertBuyerProductList(item: RoomWishlistItem){
        viewModelScope.launch {
            dao.insertWishlist(item)
        }
    }
}