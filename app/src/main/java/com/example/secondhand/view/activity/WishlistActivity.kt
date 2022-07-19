package com.example.secondhand.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.RoomWishlistItem
import com.example.secondhand.view.adapter.WishlistAdapter
import com.example.secondhand.viewmodel.RoomWishlistProductViewModel
import com.example.secondhand.viewmodel.SellerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_wishlist.*

@AndroidEntryPoint
class WishlistActivity : AppCompatActivity() {
    private lateinit var adapter: WishlistAdapter
    private lateinit var userLoginTokenManager: UserLoginTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)
        initView()
    }

    private fun initView() {
        adapter = WishlistAdapter()
        rv_wishlist.layoutManager = LinearLayoutManager(this)
        rv_wishlist.adapter = adapter

        val sellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]
        val roomWishlistProductViewModel = ViewModelProvider(this)[RoomWishlistProductViewModel::class.java]
        userLoginTokenManager = UserLoginTokenManager(this)
        userLoginTokenManager.accessToken.asLiveData().observe(this){
            sellerViewModel.getSellerData(it)
        }
        sellerViewModel.seller.observe(this){sellerData ->
            wishlist_user_name.text = sellerData.full_name + "\'s wishlist"
            roomWishlistProductViewModel.roomWishlistProduct.observe(this){wishlistValue ->
                if(wishlistValue.isNotEmpty()){
                    val listValue = mutableListOf<RoomWishlistItem>()
                    for(i in wishlistValue.indices){
                        if(wishlistValue[i].nameUser == sellerData.full_name){
                            listValue.add(wishlistValue[i])
                        }
                    }
                    adapter.setWishlistData(listValue)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}