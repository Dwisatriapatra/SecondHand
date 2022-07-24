package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetBuyerProductResponseItem
import kotlinx.android.synthetic.main.search_result_adapter_item.view.*


class SearchResultAdapter(private val onClick: (GetBuyerProductResponseItem) -> Unit) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var listBuyerProductSearch: List<GetBuyerProductResponseItem>? = null
    fun setDataBuyerProductSearch(list: List<GetBuyerProductResponseItem>) {
        this.listBuyerProductSearch = list
    }

    fun clearBuyerProductSearchData() {
        listBuyerProductSearch = null
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.search_result_adapter_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            Glide.with(card_image_produk_search.context)
                .load(listBuyerProductSearch!![position].image_url)
                .error(R.drawable.ic_launcher_background)
                .into(card_image_produk_search)

            card_product_search_nama.text = listBuyerProductSearch!![position].name
            card_product_search_harga.text =
                listBuyerProductSearch!![position].base_price.toString()

            if (listBuyerProductSearch!![position].Categories!!.isNotEmpty()) {
                for (i in listBuyerProductSearch!![position].Categories!!.indices) {
                    if (listBuyerProductSearch!![position].Categories!!.lastIndex == 0) {
                        card_product_search_kategori.text =
                            "Kategori: " + listBuyerProductSearch!![position].Categories!![i].name
                        break
                    }
                    if (i == 0) {
                        card_product_search_kategori.text =
                            "Kategori: " + listBuyerProductSearch!![position].Categories!![i].name + ", "
                    } else if (i != listBuyerProductSearch!![position].Categories!!.lastIndex && i > 0) {
                        card_product_search_kategori.text =
                            card_product_search_kategori.text.toString() + listBuyerProductSearch!![position].Categories!![i].name + ", "
                    } else {
                        card_product_search_kategori.text =
                            card_product_search_kategori.text.toString() + listBuyerProductSearch!![position].Categories!![i].name
                    }
                }
            } else {
                card_product_search_kategori.text = "Kategori: lainnya"
            }

            card_product_search.setOnClickListener {
                onClick(listBuyerProductSearch!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return if (listBuyerProductSearch.isNullOrEmpty()) {
            0
        } else {
            listBuyerProductSearch!!.size
        }
    }

}