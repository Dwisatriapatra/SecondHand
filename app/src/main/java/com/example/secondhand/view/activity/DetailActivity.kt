package com.example.secondhand.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.example.secondhand.model.PostBuyerOrder
import com.example.secondhand.viewmodel.BuyerOrderViewModel
import com.example.secondhand.viewmodel.BuyerProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.tawar_harga_bottom_sheet_dialog.view.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private lateinit var detailBarang: GetBuyerProductResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initDetailsView()

    }

    @SuppressLint("SetTextI18n")
    private fun initDetailsView() {

        if (intent.hasExtra("detailbarang")) {
            detailBarang = intent.getParcelableExtra("detailbarang")!!
        } else if (intent.hasExtra("detailbarangsearchresult")) {
            detailBarang =
                intent.getParcelableExtra("detailbarangsearchresult")!!
        }

        val viewModelBuyerProduct = ViewModelProvider(this)[BuyerProductViewModel::class.java]
        viewModelBuyerProduct.getBuyerProductById(detailBarang.id!!)
        viewModelBuyerProduct.buyerProductById.observe(this){ product ->
            //product detail
            Glide.with(this).load(product.image_url)
                .error(R.drawable.ic_launcher_background)
                .into(imgDetail)
            txtNamaBarang.text = product.name
            txtHargaBarang.text = product.base_price.toString()
            txtDeskripsi.text = product.description
            txtJenisBarang.text = ""
            if (product.Categories.isNotEmpty()) {
                for (i in product.Categories.indices) {
                    if (product.Categories.lastIndex == 0) {
                        txtJenisBarang.text = product.Categories[i].name
                        break
                    }
                    if (i == 0) {
                        txtJenisBarang.text = product.Categories[i].name + ","
                    } else if (i != product.Categories.lastIndex && i > 0) {
                        txtJenisBarang.text = txtJenisBarang.text.toString() +
                                product.Categories[i].name + ","
                    } else {
                        txtJenisBarang.text = txtJenisBarang.text.toString() +
                                product.Categories[i].name
                    }
                }
            } else {
                txtJenisBarang.text = "Lainnya"
            }

            //product seller detail
            txtNamaPenjual.text = product.User.full_name
            txtKotaPenjual.text = product.User.city
            Glide.with(imgPenjual.context)
                .load(product.User.image_url)
                .error(R.drawable.ic_launcher_background)
                .into(imgPenjual)
        }

        btnNego.setOnClickListener {
            initDialogTawarHarga()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDialogTawarHarga() {
        userLoginTokenManager = UserLoginTokenManager(this)

        userLoginTokenManager.isUser.asLiveData().observe(this) { isUser ->

            val dialog = BottomSheetDialog(this)
            val dialogView = layoutInflater.inflate(R.layout.tawar_harga_bottom_sheet_dialog, null)
            val detailbarang =
                intent.getParcelableExtra<GetBuyerProductResponseItem>("detailbarang")

            val btnBatal = dialogView.tawarDialogBatalkanButton
            val btnTawarkan = dialogView.tawarDialogTawarkanHargaButton

            dialogView.tawarDialogName.text = detailbarang!!.name
            dialogView.tawarDialogHarga.text = "Harga: Rp. ${detailbarang.base_price}"
            Glide.with(dialogView.tawarDialogImage.context)
                .load(detailbarang.image_url)
                .error(R.drawable.ic_launcher_background)
                .into(dialogView.tawarDialogImage)
            dialogView.tawarDialogKategori.text = ""
            if (detailbarang.Categories!!.isNotEmpty()) {
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

            if (isUser) {

                dialogView.tawarDialogBelumLoginLabel.isInvisible = true

                btnBatal.setOnClickListener {
                    dialog.dismiss()
                }

                btnTawarkan.setOnClickListener {
                    userLoginTokenManager.alamat.asLiveData().observe(this) { alamat ->
                        val productId = detailbarang.id
                        val edtTawar =
                            dialogView.tawarDialogInputHargaTawaran.text.toString().toInt()
                        if (alamat.isNullOrEmpty()) {
                            Toast.makeText(
                                this,
                                "Lengkapi profile anda terlebih dahulu",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(Intent(this, LengkapiInfoAkun::class.java))
                        } else {
                            if (edtTawar.toString().isNotEmpty()) {
                                val viewModelBuyerOrder =
                                    ViewModelProvider(this)[BuyerOrderViewModel::class.java]
                                userLoginTokenManager.accessToken.asLiveData()
                                    .observe(this) { accessToken ->
                                        viewModelBuyerOrder.postBuyerOrder(
                                            accessToken,
                                            PostBuyerOrder(productId!!, edtTawar)
                                        )
                                    }
                                Toast.makeText(this, "Tawaran sudah dikirim", Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Field harga tawar harus diisi",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                dialogView.tawarDialogTawarkanHargaButton.isInvisible = true
                btnBatal.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.setCancelable(false)
            dialog.setContentView(dialogView)
            dialog.show()
        }
    }
}