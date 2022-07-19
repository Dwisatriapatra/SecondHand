package com.example.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.RoomWishlistItem
import kotlinx.android.synthetic.main.item_adapter_wishlist.view.*

class WishlistAdapter : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {
    private var wishlist: List<RoomWishlistItem>? = null
    fun setWishlistData(list: List<RoomWishlistItem>){
        this.wishlist = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter_wishlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            card_wishlist_product_nama.text = wishlist!![position].name
            card_wishlist_product_harga.text = wishlist!![position].base_price.toString()
            Glide.with(card_image_wishlist_produk.context)
                .load(wishlist!![position].image_url)
                .error(R.drawable.ic_launcher_background)
                .into(card_image_wishlist_produk)
        }
    }

    override fun getItemCount(): Int {
        return if(wishlist.isNullOrEmpty()){
            0
        }else{
            wishlist!!.size
        }
    }
}