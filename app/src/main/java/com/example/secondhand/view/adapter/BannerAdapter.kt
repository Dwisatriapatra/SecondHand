package com.example.secondhand.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetSellerBannerItem
import java.util.*

class BannerAdapter(val context: Context, private var imagebanner: List<GetSellerBannerItem>) :
    PagerAdapter() {
    override fun getCount(): Int {
        return imagebanner.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mLayoutInflater.inflate(R.layout.banner_view_pager, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.vpImageBanner) as ImageView
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(context).load(imagebanner[position].imageUrl).into(imageView)

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }


}