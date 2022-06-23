package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetAllNotificationResponseItem
import kotlinx.android.synthetic.main.item_adapter_daftar_produk_ditawar.view.*

class ProdukDitawarAdapter (private val onClick: (GetAllNotificationResponseItem) -> Unit) :
    RecyclerView.Adapter<ProdukDitawarAdapter.ViewHolder>()
{
    private var listProdukDitawar: List<GetAllNotificationResponseItem>? = null
    fun setListProdukDitawar(list: List<GetAllNotificationResponseItem>){
        this.listProdukDitawar = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter_daftar_produk_ditawar, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            with(listProdukDitawar!![position]){
                card_daftar_produk_ditawar_nama_buyer.text = "Pembeli: $buyer_name"
                card_daftar_produk_ditawar_harga_tawar.text = "Harga tawar: $bid_price"
                card_daftar_produk_ditawar_status.text = "Status: $status"
                card_daftar_produk_ditawar_nama_seller.text = "Penjual: $seller_name"
                Glide.with(card_daftar_produk_ditawar_image.context)
                    .load(image_url)
                    .error(R.drawable.ic_launcher_background)
                    .override(100, 100)
                    .into(card_daftar_produk_ditawar_image)
                card_daftar_produk_ditawar_aksi_button.setOnClickListener {
                    onClick(listProdukDitawar!![position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(listProdukDitawar.isNullOrEmpty()){
            0
        }else{
            listProdukDitawar!!.size
        }
    }
}