package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetSearchProductResponseItem
import kotlinx.android.synthetic.main.search_result_adapter_item.view.*


class SearchResultAdapter(private val onClick: (GetSearchProductResponseItem) -> Unit) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private var listBuyerProductSearch: List<GetSearchProductResponseItem>? = null
    fun setDataBuyerProductSearch(list: List<GetSearchProductResponseItem>) {
        this.listBuyerProductSearch = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder{
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.search_result_adapter_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView){
            with(listBuyerProductSearch!![position]){
                Glide.with(card_image_produk_search.context)
                    .load(image_url)
                    .error(R.drawable.ic_launcher_background)
                    .into(card_image_produk_search)

                card_product_search_nama.text = name
                card_product_search_harga.text = base_price.toString()
                //card_product_search_kategori

                if(Categories.isNotEmpty()){
                    for(i in Categories.indices){
                        if (Categories.lastIndex == 0) {
                            card_product_search_kategori.text = "Kategori: " + Categories[i].name
                            break
                        }
                        if (i == 0) {
                            card_product_search_kategori.text = "Kategori: " + Categories[i].name + ", "
                        } else if (i != Categories.lastIndex && i > 0) {
                            card_product_search_kategori.text =
                                card_product_search_kategori.text.toString() + Categories[i].name + ", "
                        } else {
                            card_product_search_kategori.text =
                                card_product_search_kategori.text.toString() + Categories[i].name
                        }
                    }
                }else{
                    card_product_search_kategori.text = "Kategori: lainnya"
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return if(listBuyerProductSearch.isNullOrEmpty()){
            0
        }else{
            listBuyerProductSearch!!.size
        }
    }

}