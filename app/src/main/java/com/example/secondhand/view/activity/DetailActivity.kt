package com.example.secondhand.view.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.example.secondhand.model.PostBuyerOrder
import com.example.secondhand.model.RoomWishlistItem
import com.example.secondhand.viewmodel.BuyerOrderViewModel
import com.example.secondhand.viewmodel.BuyerProductViewModel
import com.example.secondhand.viewmodel.RoomWishlistProductViewModel
import com.example.secondhand.viewmodel.SellerViewModel
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
        viewModelBuyerProduct.buyerProductById.observe(this) { product ->
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

        btnTambahkanKeWishlist.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Menambahkan ke wishlist")
                .setMessage("Anda yakin ingin menambahkan product ini ke wishlist?")
                .setNegativeButton("Tidak") { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("Ya") { _: DialogInterface, _: Int ->
                    val viewModelWishlistProduct =
                        ViewModelProvider(this)[RoomWishlistProductViewModel::class.java]
                    val sellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]
                    userLoginTokenManager = UserLoginTokenManager(this)

                    userLoginTokenManager.accessToken.asLiveData().observe(this){
                        sellerViewModel.getSellerData(it)
                    }
                    viewModelBuyerProduct.getBuyerProductById(detailBarang.id!!)

                    sellerViewModel.seller.observe(this){sellerData ->
                        viewModelBuyerProduct.buyerProductById.observe(this) { product ->
                            viewModelWishlistProduct.insertBuyerProductList(
                                RoomWishlistItem(
                                    null,
                                    sellerData.full_name,
                                    product.name,
                                    product.base_price,
                                    product.image_url
                                )
                            )
                            Toast.makeText(this@DetailActivity, "Berhasil ditambahkan ke wishlist", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initDialogTawarHarga() {
        userLoginTokenManager = UserLoginTokenManager(this)
        val viewModelSellerData = ViewModelProvider(this)[SellerViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(this) { accessToken ->
            viewModelSellerData.getSellerData(accessToken)
        }
        userLoginTokenManager.isUser.asLiveData().observe(this) { isUser ->

            val dialog = BottomSheetDialog(this)
            val dialogView = layoutInflater.inflate(R.layout.tawar_harga_bottom_sheet_dialog, null)

            if (intent.hasExtra("detailbarangsearchresult")) {
                detailBarang =
                    intent.getParcelableExtra("detailbarangsearchresult")!!
            } else if (intent.hasExtra("detailbarang")) {
                detailBarang =
                    intent.getParcelableExtra("detailbarang")!!
            }

            val btnBatal = dialogView.tawarDialogBatalkanButton
            val btnTawarkan = dialogView.tawarDialogTawarkanHargaButton

            dialogView.tawarDialogName.text = detailBarang.name
            dialogView.tawarDialogHarga.text = "Harga: Rp. ${detailBarang.base_price}"
            Glide.with(dialogView.tawarDialogImage.context)
                .load(detailBarang.image_url)
                .error(R.drawable.ic_launcher_background)
                .into(dialogView.tawarDialogImage)
            dialogView.tawarDialogKategori.text = ""
            if (detailBarang.Categories!!.isNotEmpty()) {
                for (i in detailBarang.Categories?.indices!!) {
                    if (detailBarang.Categories?.lastIndex == 0) {
                        dialogView.tawarDialogKategori.text =
                            "Kategori: ${detailBarang.Categories!![i].name}"
                        break
                    }
                    if (i == 0) {
                        dialogView.tawarDialogKategori.text =
                            "Kategori: ${detailBarang.Categories!![i].name}, "
                    } else if (i != detailBarang.Categories!!.lastIndex && i > 0) {
                        dialogView.tawarDialogKategori.text =
                            dialogView.tawarDialogKategori.text.toString() + detailBarang.Categories!![i].name + ","
                    } else {
                        dialogView.tawarDialogKategori.text =
                            dialogView.tawarDialogKategori.text.toString() +
                                    detailBarang.Categories!![i].name
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
                    viewModelSellerData.seller.observe(this) { seller ->
                        val productId = detailBarang.id
                        val edtTawar =
                            dialogView.tawarDialogInputHargaTawaran.text.toString().toInt()

                        if (seller.address.isEmpty()) {
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