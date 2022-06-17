package com.example.secondhand.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tawar_harga_bottom_sheet_dialog.view.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initDetailsView()

    }

    @SuppressLint("SetTextI18n")
    private fun initDetailsView() {
        val detailbarang = intent.getParcelableExtra<GetBuyerProductResponseItem>("detailbarang")

        if (detailbarang != null) {
            Glide.with(this).load(detailbarang.image_url).into(imgDetail)
            txtNamaBarang.text = detailbarang.name
            txtJenisBarang.text = detailbarang.Categories.toString()
            txtHargaBarang.text = detailbarang.base_price.toString()
            txtDeskripsi.text = detailbarang.description


            txtJenisBarang.text = ""
            if (detailbarang.Categories.isNotEmpty()) {
                for (i in detailbarang.Categories.indices) {
                    if (detailbarang.Categories.lastIndex == 0) {
                        txtJenisBarang.text = detailbarang.Categories[i].name
                        break
                    }
                    if (i == 0) {
                        txtJenisBarang.text = detailbarang.Categories[i].name + ","
                    } else if (i != detailbarang.Categories.lastIndex && i > 0) {
                        txtJenisBarang.text = txtJenisBarang.text.toString() +
                                detailbarang.Categories[i].name + ","
                    } else {
                        txtJenisBarang.text = txtJenisBarang.text.toString() +
                                detailbarang.Categories[i].name
                    }
                }
            } else {
                txtJenisBarang.text = "Lainnya"
            }
        }

        btnNego.setOnClickListener {
            initDialogTawarHarga()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initDialogTawarHarga() {
        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.tawar_harga_bottom_sheet_dialog, null)
        val detailbarang = intent.getParcelableExtra<GetBuyerProductResponseItem>("detailbarang")

        val btnBatal = dialogView.tawarDialogBatalkanButton
        val btnTawarkan = dialogView.tawarDialogTawarkanHargaButton
        dialogView.tawarDialogName.text = detailbarang!!.name
        dialogView.tawarDialogHarga.text = "Harga: Rp. ${detailbarang.base_price}"
        Glide.with(dialogView.tawarDialogImage.context)
            .load(detailbarang.image_url)
            .error(R.drawable.ic_launcher_background)
            .into(dialogView.tawarDialogImage)
        dialogView.tawarDialogKategori.text = ""
        if (detailbarang.Categories.isNotEmpty()) {
            for (i in detailbarang.Categories.indices) {
                if (detailbarang.Categories.lastIndex == 0) {
                    dialogView.tawarDialogKategori.text =
                        "Kategori: ${detailbarang.Categories[i].name}"
                    break
                }
                if (i == 0) {
                    dialogView.tawarDialogKategori.text =
                        "Kategori: ${detailbarang.Categories[i].name}, "
                } else if (i != detailbarang.Categories.lastIndex && i > 0) {
                    dialogView.tawarDialogKategori.text =
                        dialogView.tawarDialogKategori.text.toString() + detailbarang.Categories[i].name + ","
                } else {
                    dialogView.tawarDialogKategori.text =
                        dialogView.tawarDialogKategori.text.toString() +
                                detailbarang.Categories[i].name
                }
            }
        } else {
            dialogView.tawarDialogKategori.text = "Kategori: Lainnya"
        }

        btnBatal.setOnClickListener {
            dialog.dismiss()
        }

        btnTawarkan.setOnClickListener {
            //do something
            //edit
        }
        dialog.setCancelable(false)
        dialog.setContentView(dialogView)
        dialog.show()
    }
}