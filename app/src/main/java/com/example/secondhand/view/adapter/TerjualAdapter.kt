package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetSellerOrderResponseItem
import kotlinx.android.synthetic.main.item_adapter_terjual.view.*

class TerjualAdapter : RecyclerView.Adapter<TerjualAdapter.ViewHolder>() {
    private var listProdukTerjual : List<GetSellerOrderResponseItem>? = null
    fun setListProdukTerjual(list: List<GetSellerOrderResponseItem>){
        this.listProdukTerjual = list
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter_terjual, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            card_daftar_jual_terjual_nama_seller.text = "Nama penjual: ${listProdukTerjual!![position].Product.User.full_name}"
            card_daftar_jual_terjual_nama_buyer.text = "Nama pembeli: ${listProdukTerjual!![position].User.full_name}"
            card_daftar_jual_terjual_harga_tawar.text = "Harga tawar: ${listProdukTerjual!![position].price}"
            card_daftar_jual_terjual_status.text = "Status produk: ${listProdukTerjual!![position].Product.status}"
            card_daftar_jual_terjual_nama_produk.text = "Nama produk: ${listProdukTerjual!![position].Product.name}"
            Glide.with(card_daftar_jual_terjual_image.context)
                .load(listProdukTerjual!![position].image_product)
                .error(R.drawable.ic_launcher_background)
                .into(card_daftar_jual_terjual_image)
        }
    }

    override fun getItemCount(): Int {
        return if(listProdukTerjual.isNullOrEmpty()){
            0
        }else{
            listProdukTerjual!!.size
        }
    }

}