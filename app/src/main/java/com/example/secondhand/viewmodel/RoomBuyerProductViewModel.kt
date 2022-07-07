package com.example.secondhand.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secondhand.model.RoomBuyerProduct
import com.example.secondhand.roomdatabase.BuyerProductDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomBuyerProductViewModel @Inject constructor(buyerProductDao: BuyerProductDao) : ViewModel(){
    private val liveDataRoomBuyerProduct = MutableLiveData<RoomBuyerProduct>()
    val roomBuyerProduct : LiveData<RoomBuyerProduct> = liveDataRoomBuyerProduct

    private val dao = buyerProductDao
    init {
        viewModelScope.launch {
            val dataBuyerProduct = buyerProductDao.roomGetAllBuyerProductList()
            liveDataRoomBuyerProduct.value = dataBuyerProduct
        }
    }

    fun insertBuyerProductList(list: RoomBuyerProduct){
        viewModelScope.launch {
            dao.roomInsertBuyerProductList(list)
        }
    }
}