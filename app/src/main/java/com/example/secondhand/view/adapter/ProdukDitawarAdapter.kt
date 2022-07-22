package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.helper.PenawaranItemClickListener
import com.example.secondhand.model.GetSellerOrderResponseItem
import kotlinx.android.synthetic.main.item_adapter_daftar_produk_ditawar.view.*

class ProdukDitawarAdapter(
    private val penawaranItemClickListener: PenawaranItemClickListener,
    private val namaSeller: String
) :
    RecyclerView.Adapter<ProdukDitawarAdapter.ViewHolder>() {
    private var listProdukDitawar: List<GetSellerOrderResponseItem>? = null
    fun setListProdukDitawar(list: List<GetSellerOrderResponseItem>) {
        this.listProdukDitawar = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_adapter_daftar_produk_ditawar, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            card_daftar_produk_ditawar_nama_buyer.text =
                "Pembeli: ${listProdukDitawar!![position].User.full_name}e"
            card_daftar_produk_ditawar_harga_tawar.text =
                "Harga tawar: ${listProdukDitawar!![position].price}"
            card_daftar_produk_ditawar_status.text =
                "Status: ${listProdukDitawar!![position].status}"
            card_daftar_produk_ditawar_nama_seller.text = "Penjual: $namaSeller"
            Glide.with(card_daftar_produk_ditawar_image.context)
                .load(listProdukDitawar!![position].Product.image_url)
                .error(R.drawable.ic_launcher_background)
                .override(100, 100)
                .into(card_daftar_produk_ditawar_image)
            if (listProdukDitawar!![position].status == "accepted" ||
                listProdukDitawar!![position].status == "diterima" ||
                listProdukDitawar!![position].status == "declined" ||
                listProdukDitawar!![position].status == "ditolak" ||
                listProdukDitawar!![position].status == "sold") {
                card_daftar_produk_ditawar_terima_tolak_button_section.isInvisible = true
            } else {
                card_daftar_produk_ditawar_status_hubungi_button_section.isInvisible = true
            }
            card_daftar_produk_ditawar_tolak_button.setOnClickListener {
                penawaranItemClickListener.tolakButton(listProdukDitawar!![position], position)
            }
            card_daftar_produk_ditawar_terima_button.setOnClickListener {
                penawaranItemClickListener.terimaButton(listProdukDitawar!![position], position)
            }
            card_daftar_produk_ditawar_hubungi_button.setOnClickListener {
                penawaranItemClickListener.hubungiButton(listProdukDitawar!![position], position)
            }
            card_daftar_produk_ditawar_status_button.setOnClickListener {
                penawaranItemClickListener.statusButton(listProdukDitawar!![position], position)
            }
        }

    }

    override fun getItemCount(): Int {
        return if (listProdukDitawar.isNullOrEmpty()) {
            0
        } else {
            listProdukDitawar!!.size
        }
    }
}