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
            card_notification_seller.text = "Penjual: ${listNotification!![position].seller_name}"
            if (listNotification!![position].Product == null) {
                card_notification_nama_produk.text =
                    "Nama produk: (Produk sudah tidak di jual)"
            } else {
                card_notification_nama_produk.text =
                    "Nama produk: ${listNotification!![position].Product!!.name}"
            }
            card_notification_read_status.isInvisible = listNotification!![position].read
            if (listNotification!![position].status == "create") {
                card_notification_buyer.text = "Harga: ${listNotification!![position].base_price}"
                card_notification_status.text = ""
                card_notification_harga_tawar.text = ""
                card_notification_label_transaksi.text = "Penambahan Produk Baru"
            } else {
                card_notification_buyer.text = "Pembeli: ${listNotification!![position].buyer_name}"
                card_notification_status.text = "Status: Ditawar"
                card_notification_harga_tawar.text =
                    "Harga tawar: ${listNotification!![position].bid_price}"
                card_notification_label_transaksi.text = "Penawaran Produk"
            }
            if (listNotification!![position].transaction_date == null) {
                card_notification_tanggal_transaksi.text = listNotification!![position].createdAt
            } else {
                card_notification_tanggal_transaksi.text =
                    listNotification!![position].transaction_date
            }

            Glide.with(card_notification_image.context)
                .load(listNotification!![position].image_url)
                .error(R.drawable.ic_launcher_background)
                .override(75, 75)
                .into(card_notification_image)


            card_notification_read_status.setOnClickListener {
                notificationItemClickListener.clickOnNotificationReadStatus(
                    listNotification!![position],
                    position
                )
            }
            card_notification_body.setOnClickListener {
                notificationItemClickListener.clickOnNotificationBodySection(
                    listNotification!![position],
                    position
                )
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