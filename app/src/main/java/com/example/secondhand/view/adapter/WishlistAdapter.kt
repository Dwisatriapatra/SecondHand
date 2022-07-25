package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetBuyerWishlistResponseItem
import kotlinx.android.synthetic.main.item_adapter_wishlist.view.*

class WishlistAdapter(private val onClick: (GetBuyerWishlistResponseItem) -> Unit) :
    RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {
    private var wishlist: List<GetBuyerWishlistResponseItem>? = null
    fun setWishlistData(list: List<GetBuyerWishlistResponseItem>) {
        this.wishlist = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_wishlist, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            card_wishlist_product_nama.text = wishlist!![position].Product.name
            card_wishlist_product_harga.text =
                "Harga produk: ${wishlist!![position].Product.base_price}"
            card_wishlist_product_lokasi_seller.text =
                "Lokasi penjual: ${wishlist!![position].Product.location}"
            Glide.with(card_image_wishlist_produk.context)
                .load(wishlist!![position].Product.image_url)
                .error(R.drawable.ic_launcher_background)
                .into(card_image_wishlist_produk)
            card_wishlist_product_delete_button.setOnClickListener {
                onClick(wishlist!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (wishlist.isNullOrEmpty()) {
            0
        } else {
            wishlist!!.size
        }
    }
}