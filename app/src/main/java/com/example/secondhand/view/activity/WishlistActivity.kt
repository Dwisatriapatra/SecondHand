package com.example.secondhand.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.isOnline
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
        swipe_refresh_wishlist.setOnRefreshListener {
            swipe_refresh_wishlist.isRefreshing = false
            refreshCurrentActivity()
        }
    }

    private fun initView() {
        adapter = WishlistAdapter()
        rv_wishlist.layoutManager = LinearLayoutManager(this)
        rv_wishlist.adapter = adapter


        userLoginTokenManager = UserLoginTokenManager(this)

        userLoginTokenManager.isUser.asLiveData().observe(this){isUser ->
            if(isOnline(this)){
                if(isUser){
                    wishlist_belum_login_section.isInvisible = true
                    val sellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]
                    val roomWishlistProductViewModel =
                        ViewModelProvider(this)[RoomWishlistProductViewModel::class.java]
                    userLoginTokenManager.accessToken.asLiveData().observe(this) {
                        sellerViewModel.getSellerData(it)
                    }
                    sellerViewModel.seller.observe(this) { sellerData ->
                        wishlist_user_name.text = sellerData.full_name + "\'s wishlist"
                        roomWishlistProductViewModel.roomWishlistProduct.observe(this) { wishlistValue ->
                            if (wishlistValue.isNotEmpty()) {
                                wishlist_no_data_animation.isInvisible = true
                                val listValue = mutableListOf<RoomWishlistItem>()
                                for (i in wishlistValue.indices) {
                                    if (wishlistValue[i].nameUser == sellerData.full_name) {
                                        listValue.add(wishlistValue[i])
                                    }
                                }
                                adapter.setWishlistData(listValue)
                                adapter.notifyDataSetChanged()
                            }else{
                                wishlist_no_data_animation.isInvisible = false
                            }
                        }
                    }
                }else{
                    wishlist_no_data_animation.isInvisible = true
                    wishlist_belum_login_section.isInvisible = false
                }
            }else{
                wishlist_no_data_animation.isInvisible = true
                wishlist_belum_login_section.isInvisible = true
                wishlist_user_name.isInvisible = true
            }
        }
    }

    // reload current activity function
    private fun refreshCurrentActivity(){
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }
}