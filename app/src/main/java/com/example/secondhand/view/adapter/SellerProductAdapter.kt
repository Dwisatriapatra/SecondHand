package com.example.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetSellerProductItem
import kotlinx.android.synthetic.main.item_adapter_seller_product_item.view.*

class SellerProductAdapter(private val onClick: (GetSellerProductItem) -> Unit) :
    RecyclerView.Adapter<SellerProductAdapter.ViewHolder>() {

    private var listSellerProduct: List<GetSellerProductItem>? = null
    fun setDataSellerProduct(list: List<GetSellerProductItem>) {
        this.listSellerProduct = list
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_seller_product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            with(listSellerProduct!![position]) {
                card_daftar_jual_produk_nama.text = name
                card_daftar_jual_produk_harga.text = base_price.toString()
                Glide.with(card_daftar_jual_produk_image.context)
                    .load(image_url)
                    .error(R.drawable.ic_launcher_background)
                    .override(72, 72)
                    .into(card_daftar_jual_produk_image)
                card_daftar_jual_produk_lokasi.text = location

                card_seller_product.setOnClickListener {
                    onClick(listSellerProduct!![position])
                }

            }
        }

    }

    override fun getItemCount(): Int {
        return if (listSellerProduct.isNullOrEmpty()) {
            0
        } else {
            listSellerProduct!!.size
        }
    }
}