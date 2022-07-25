package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetAllNotificationResponseItem
import kotlinx.android.synthetic.main.item_adapter_diminati.view.*

class DiminatiAdapter(private val onClick: (GetAllNotificationResponseItem) -> Unit) :
    RecyclerView.Adapter<DiminatiAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var listData: List<GetAllNotificationResponseItem>? = null
    fun setDiminatiData(list: List<GetAllNotificationResponseItem>) {
        this.listData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_diminati, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            card_daftar_jual_diminati_nama_seller.text =
                "Penjual: ${listData!![position].seller_name}"
            card_daftar_jual_diminati_nama_buyer.text =
                "Pembeli: ${listData!![position].buyer_name}"
            card_daftar_jual_diminati_status.text = "Status: Ditawar"
            card_daftar_jual_diminati_harga_tawar.text =
                "Harga tawar: Rp. ${listData!![position].bid_price}"
            card_daftar_jual_diminati_tanggal_transaksi.text = listData!![position].transaction_date
            if (listData!![position].Product == null) {
                card_daftar_jual_diminati_nama_product.text =
                    "Produk: (Produk sudah tidak dijual)"
            } else {
                card_daftar_jual_diminati_nama_product.text =
                    "Produk: ${listData!![position].Product!!.name}"
            }
            if (listData!![position].status == "bid") {
                card_daftar_jual_diminati_label_transaksi.text = "Penawaran produk"
            }
            Glide.with(card_daftar_jual_diminati_image.context)
                .load(listData!![position].image_url)
                .error(R.drawable.ic_launcher_background)
                .override(75, 75)
                .into(card_daftar_jual_diminati_image)


            card_daftar_jual_diminati.setOnClickListener {
                onClick(listData!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (listData.isNullOrEmpty()) {
            0
        } else {
            listData!!.size
        }
    }
}