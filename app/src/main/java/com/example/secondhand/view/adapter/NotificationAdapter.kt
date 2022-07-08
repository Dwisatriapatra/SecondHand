package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.helper.NotificationItemClickListener
import com.example.secondhand.model.GetAllNotificationResponseItem
import kotlinx.android.synthetic.main.item_adapter_notification.view.*


class NotificationAdapter(private val notificationItemClickListener: NotificationItemClickListener) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var listNotification: List<GetAllNotificationResponseItem>? = null
    fun setNotificationData(list: List<GetAllNotificationResponseItem>) {
        this.listNotification = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_notification, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            with(listNotification!![position]) {
                card_notification_seller.text = "Penjual: $seller_name"
                card_notification_buyer.text = "Pembeli: $buyer_name"
                card_notification_status.text = "Status: $status"
                card_notification_harga_tawar.text = "Harga tawar: $bid_price"
                card_notification_tanggal_transaksi.text = transaction_date
                Glide.with(card_notification_image.context)
                    .load(image_url)
                    .error(R.drawable.ic_launcher_background)
                    .override(75, 75)
                    .into(card_notification_image)

                if(read == true){
                    card_notification_read_status.isInvisible = true
                }
                card_notification_read_status.setOnClickListener {
                    notificationItemClickListener.clickOnNotificationReadStatus(listNotification!![position], position)
                }
                card_notification_body.setOnClickListener{
                    notificationItemClickListener.clickOnNotificationBodySection(listNotification!![position], position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (listNotification.isNullOrEmpty()) {
            0
        } else {
            listNotification!!.size
        }
    }
}