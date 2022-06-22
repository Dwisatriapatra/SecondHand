package com.example.secondhand.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetAllNotificationResponseItem
import kotlinx.android.synthetic.main.item_adapter_notification.view.*


class NotificationAdapter :
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            with(listNotification!![position]) {
                card_notification_seller.text = seller_name
                card_notification_buyer.text = buyer_name
                card_notification_status.text = status
                Glide.with(card_notification_image.context)
                    .load(image_url)
                    .error(R.drawable.ic_launcher_background)
                    .override(75, 75)
                    .into(card_notification_image)
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