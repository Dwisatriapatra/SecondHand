package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetAllUserHistoryResponseItem
import kotlinx.android.synthetic.main.item_adapter_user_history.view.*


class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    private var historyList : List<GetAllUserHistoryResponseItem>? = null
    fun setDataUserHistory(list: List<GetAllUserHistoryResponseItem>){
        this.historyList = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_adapter_user_history, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            card_history_product_nama.text = historyList!![position].product_name
            card_history_product_transaction_date.text = "Tanggal transaksi: ${historyList!![position].transaction_date}"
            card_history_status.text = "Status: ${historyList!![position].status}"
            Glide.with(card_history_image_produk.context)
                .load(historyList!![position].image_url)
                .error(R.drawable.ic_launcher_background)
                .into(card_history_image_produk)
        }
    }

    override fun getItemCount(): Int {
        return if(historyList.isNullOrEmpty()){
            0
        }else{
            historyList!!.size
        }
    }

}