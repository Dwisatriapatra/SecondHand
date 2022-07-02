package com.example.secondhand.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetBuyerProductResponseItem
import kotlinx.android.synthetic.main.custom_list_view_search_result.view.*


class SearchListResultAdapter(context: Context, arrayList: ArrayList<GetBuyerProductResponseItem>, private val onClick: (GetBuyerProductResponseItem) -> Unit) :
    ArrayAdapter<GetBuyerProductResponseItem>(context, 0, arrayList) {
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var currentItemView: View? = convertView

        if(currentItemView == null){
            currentItemView = LayoutInflater.from(context).inflate(R.layout.custom_list_view_search_result, parent, false)
        }

        val currentNumberPosition: GetBuyerProductResponseItem = getItem(position)!!
        Glide.with(currentItemView!!.card_search_result_image_product.context)
            .load(currentNumberPosition.image_url)
            .error(R.drawable.ic_launcher_background)
            .into(currentItemView.card_search_result_image_product)

        currentItemView.card_search_result_nama_product.text = currentNumberPosition.name
        currentItemView.card_search_result_harga_product.text =
            currentNumberPosition.base_price.toString()

        for (i in currentNumberPosition.Categories!!.indices) {
            if (currentNumberPosition.Categories.lastIndex == 0) {
                currentItemView.card_search_result_kategori_product.text =
                    "Kategori: ${currentNumberPosition.Categories[i].name}"
                break
            }
            if (i == 0) {
                currentItemView.card_search_result_kategori_product.text =
                    "Kategori: " + currentNumberPosition.Categories[i].name + ", "
            } else if (i != currentNumberPosition.Categories.lastIndex && i > 0) {
                currentItemView.card_search_result_kategori_product.text =
                    currentItemView.card_search_result_kategori_product.text.toString() +
                            currentNumberPosition.Categories[i].name + ", "
            } else {
                currentItemView.card_search_result_kategori_product.text =
                    currentItemView.card_search_result_kategori_product.text.toString() +
                            currentNumberPosition.Categories[i].name
            }
        }

        currentItemView.card_search_result.setOnClickListener {
            onClick(currentNumberPosition)
        }

        return currentItemView
    }
}