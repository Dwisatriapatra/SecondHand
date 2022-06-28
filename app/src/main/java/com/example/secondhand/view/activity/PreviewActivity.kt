package com.example.secondhand.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.PostJualProduct
import com.example.secondhand.viewmodel.SellerJualProductViewModel
import com.example.secondhand.viewmodel.SellerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_preview.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {

    private lateinit var userLoginTokenManager : UserLoginTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        initPreview()

    }

    private fun initPreview() {
        val dataProduk = intent.getSerializableExtra("dataprodukjual") as PostJualProduct
        val viewModelSeller = ViewModelProvider(this)[SellerViewModel::class.java]

        previewImageProduct.setImageURI(Uri.parse(dataProduk.imageUri))
        previewNamaBarang.text = dataProduk.namaBarang
        previewKategoriBarang.text = dataProduk.kategoriProduk
        previewHargaBarang.text = dataProduk.harga.toString()
        previewDeskripsiProduct.text = dataProduk.deskripsiProduct
        //seller
        userLoginTokenManager = UserLoginTokenManager(this)
        userLoginTokenManager.accessToken.asLiveData().observe(this){tokenAccess ->
            viewModelSeller.getSeller(tokenAccess)
            viewModelSeller.seller.observe(this){
                previewNamaSeller.text = it.full_name
                previewKotaSeller.text = it.city
                Glide.with(previewImageSeller.context)
                    .load(it.image_url)
                    .error(R.drawable.ic_launcher_background)
                    .into(previewImageSeller)
            }
        }

        previewTerbitkanButton.setOnClickListener {
            val viewModelSellerJualProduct =
                ViewModelProvider(this)[SellerJualProductViewModel::class.java]

            val namaProduk =
                dataProduk.namaBarang.toRequestBody("multipart/form-data".toMediaType())
            val hargaProduk =
                dataProduk.harga.toString().toRequestBody("multipart/form-data".toMediaType())
            val kategori = listOf(1).toString().toRequestBody("multipart/form-data".toMediaType())
            val deskripsiProduk =
                dataProduk.deskripsiProduct.toRequestBody("multipart/form-data".toMediaType())

            val file = dataProduk.imageProduct
            val requestImageFile = file.asRequestBody("multipart/form-data".toMediaType())
            val imageProduk = MultipartBody.Part.createFormData("image", file.name, requestImageFile)

            val lokasiToko = dataProduk.lokasi.toRequestBody("multipart/form-data".toMediaType())

            userLoginTokenManager.accessToken.asLiveData().observe(this) {
                viewModelSellerJualProduct.jualProduct(
                    it,
                    hargaProduk,
                    kategori,
                    deskripsiProduk,
                    imageProduk,
                    lokasiToko,
                    namaProduk
                )
                viewModelSellerJualProduct.responseMessage.observe(this) { responseMsg ->
                    if (responseMsg == true) {
                        Toast.makeText(
                            this,
                            "Barang berhasil dijual",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }


        }
    }
}