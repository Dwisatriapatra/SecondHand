package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetAllNotificationResponseItem
import kotlinx.android.synthetic.main.item_adapter_diminati_terjual.view.*

class DiminatiTerjualAdapter(private val onClick: (GetAllNotificationResponseItem) -> Unit) : RecyclerView.Adapter<DiminatiTerjualAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private var listData: List<GetAllNotificationResponseItem>? = null
    fun setDiminatiTerjualData(list: List<GetAllNotificationResponseItem>) {
        this.listData = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_diminati_terjual, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            with(listData!![position]) {
                card_diminati_terjual_seller.text = "Penjual: $seller_name"
                card_diminati_terjual_buyer.text = "Pembeli: $buyer_name"
                card_diminati_terjual_status.text = "Status: $status"
                card_diminati_terjual_harga_tawar.text = "Harga tawar: $bid_price"
                card_diminati_terjual_tanggal_transaksi.text = transaction_date
                Glide.with(card_diminati_terjual_image.context)
                    .load(image_url)
                    .error(R.drawable.ic_launcher_background)
                    .override(75, 75)
                    .into(card_diminati_terjual_image)
                card_diminati_terjual.setOnClickListener {
                    onClick(listData!![position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(listData.isNullOrEmpty()){
            0
        }else{
            listData!!.size
        }
    }
}