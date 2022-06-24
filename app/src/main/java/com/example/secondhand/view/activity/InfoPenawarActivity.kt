package com.example.secondhand.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.GetAllNotificationResponseItem
import com.example.secondhand.model.OrderStatus
import com.example.secondhand.view.adapter.ProdukDitawarAdapter
import com.example.secondhand.viewmodel.NotificationViewModel
import com.example.secondhand.viewmodel.SellerOrderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_info_penawar.*
import kotlinx.android.synthetic.main.hubungi_via_whatsapp_bottom_sheet.view.*


@AndroidEntryPoint
class InfoPenawarActivity : AppCompatActivity() {

    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private lateinit var adapter: ProdukDitawarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_penawar)

        val dataPenawar = intent.getParcelableExtra<GetAllNotificationResponseItem>("InfoPenawaran")

        initRecyclerView(dataPenawar!!.seller_name, dataPenawar.buyer_name, dataPenawar.image_url)
    }

    private fun initRecyclerView(sellerName: String?, buyerName: String?, imageUrl: String?) {

        info_penawar_nama_pembeli.text = buyerName
        Glide.with(info_penawar_foto_pembeli.context)
            .load(imageUrl)
            .error(R.drawable.ic_launcher_background)
            .override(100, 100)
            .into(info_penawar_foto_pembeli)

        userLoginTokenManager = UserLoginTokenManager(this)
        val viewModelProdukDitawar = ViewModelProvider(this)[NotificationViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(this) {
            viewModelProdukDitawar.getAllNotification(it)
        }
        val viewModelSellerOrder = ViewModelProvider(this)[SellerOrderViewModel::class.java]
        adapter = ProdukDitawarAdapter {
            AlertDialog.Builder(this)
                .setTitle("Terima/tolak tawaran")
                .setMessage("Silahkan memilih untuk menerima/menolak tawaran")
                .setNegativeButton("Tolak") { dialogInterface: DialogInterface, _: Int ->
                    userLoginTokenManager.accessToken.asLiveData().observe(this) { acessToken ->
                        viewModelSellerOrder.setOrderStatus(
                            acessToken,
                            it.product_id!!,
                            OrderStatus("Ditolak")
                        )
                        viewModelSellerOrder.responseMessage.observe(this) {
                            if (it) {
                                Toast.makeText(this, "Berhasil ditolak", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Permintaan anda gagal", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        dialogInterface.dismiss()
                    }
                }
                .setPositiveButton("Terima") { dialoInterface: DialogInterface, _: Int ->
                    userLoginTokenManager.accessToken.asLiveData().observe(this) { acessToken ->
                        viewModelSellerOrder.setOrderStatus(
                            acessToken,
                            it.product_id!!,
                            OrderStatus("Diterima")
                        )
                        viewModelSellerOrder.responseMessage.observe(this) { bool ->
                            if (bool) {
                                Toast.makeText(this, "Berhasil diterima", Toast.LENGTH_SHORT).show()
                                initDialogToWhatsApp(it.image_url, it.bid_price)
                            } else {
                                Toast.makeText(this, "Permintaan anda gagal", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        dialoInterface.dismiss()
                    }
                }
                .show()
        }
        rv_daftar_barang_ditawar.layoutManager = LinearLayoutManager(this)
        rv_daftar_barang_ditawar.adapter = adapter


        viewModelProdukDitawar.notification.observe(this) {
            val list: MutableList<GetAllNotificationResponseItem> = mutableListOf()
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    if (it[i].seller_name == sellerName && it[i].buyer_name == buyerName) {
                        list += it[i]
                    }
                }
                adapter.setListProdukDitawar(list)
                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun initDialogToWhatsApp(imageUrl: String?, bidPrice: Int?) {
        userLoginTokenManager = UserLoginTokenManager(this)

        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.hubungi_via_whatsapp_bottom_sheet, null)
        val dataPenawar = intent.getParcelableExtra<GetAllNotificationResponseItem>("InfoPenawaran")

        dialogView.txtNamaPembeliWhatsApp.text = "${dataPenawar!!.buyer_name}"
        dialogView.txtKotaPembeliWhatsApp.text = ""
        Glide.with(dialogView.imgPembeliWhatsApp.context)
            .load(dataPenawar.image_url)
            .error(R.drawable.ic_launcher_background)
            .override(100, 100)
            .into(dialogView.imgPembeliWhatsApp)

        dialogView.txtHargaTawarWhatsApp.text = "Harga tawar: Rp. $bidPrice"
        Glide.with(dialogView.imgBarangWhatsApp.context)
            .load(imageUrl)
            .error(R.drawable.ic_launcher_background)
            .override(100, 100)
            .into(dialogView.imgBarangWhatsApp)

        dialogView.btnHubungiViaWHatsApp.setOnClickListener {
            //do intent to whatsapp
            val number = "+6281248400760"
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }
}