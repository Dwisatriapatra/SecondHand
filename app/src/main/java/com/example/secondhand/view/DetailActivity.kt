package com.example.secondhand.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.Category
import com.example.secondhand.model.GetBuyerProductResponseItem
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var image : String
    lateinit var name : String
    lateinit var category : String
    lateinit var base_price : String
    lateinit var description : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val detailbarang = intent.getParcelableExtra<GetBuyerProductResponseItem>("detailbarang")

        if(detailbarang != null){
            Glide.with(this).load(detailbarang.image_url).into(imgDetail)
            txtNamaBarang.text = detailbarang.name
            txtJenisBarang.text = detailbarang.Categories.toString()
            txtHargaBarang.text = detailbarang.base_price.toString()
            txtDeskripsi.text = detailbarang.description


            txtJenisBarang.text = ""
            if (detailbarang.Categories.isNotEmpty()){
                for (i in detailbarang.Categories.indices){
                    if (detailbarang.Categories.lastIndex == 0){
                        txtJenisBarang.text = detailbarang.Categories[i].name
                        break;
                    }
                    if(i == 0){
                        txtJenisBarang.text = detailbarang.Categories[i].name + ","
                    }else if(i != detailbarang.Categories.lastIndex && i > 0){
                        txtJenisBarang.text = txtJenisBarang.text.toString() +
                                detailbarang.Categories[i].name + ","
                    }else{
                        txtJenisBarang.text = txtJenisBarang.text.toString() +
                                detailbarang.Categories[i].name
                    }
                }
            }else{
                txtJenisBarang.text = "Kategori belum ada"
            }
        }
    }
}