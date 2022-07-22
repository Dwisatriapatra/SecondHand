package com.example.secondhand.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.PenawaranItemClickListener
import com.example.secondhand.model.GetAllNotificationResponseItem
import com.example.secondhand.model.GetSellerOrderResponseItem
import com.example.secondhand.model.OrderStatus
import com.example.secondhand.view.adapter.ProdukDitawarAdapter
import com.example.secondhand.viewmodel.SellerOrderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_info_penawar.*
import kotlinx.android.synthetic.main.hubungi_via_whatsapp_bottom_sheet.view.*
import kotlinx.android.synthetic.main.status_penjualan_bottom_sheet.view.*
import java.net.URLEncoder


@AndroidEntryPoint
class InfoPenawarActivity : AppCompatActivity(), PenawaranItemClickListener {

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
        val viewModelSellerOrder = ViewModelProvider(this)[SellerOrderViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(this) {
            viewModelSellerOrder.getAllSellerOrder(it)
        }
        adapter = ProdukDitawarAdapter(this@InfoPenawarActivity, sellerName!!)
        rv_daftar_barang_ditawar.layoutManager = LinearLayoutManager(this)
        rv_daftar_barang_ditawar.adapter = adapter
        viewModelSellerOrder.sellerOrder.observe(this) {
            val list: MutableList<GetSellerOrderResponseItem> = mutableListOf()
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    if (it[i].User.full_name == buyerName) {
                        list += it[i]
                    }
                }
                adapter.setListProdukDitawar(list)
                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun initDialogToWhatsApp(imageUrl: String?, bidPrice: Int?, buyerPhone: String?) {
        userLoginTokenManager = UserLoginTokenManager(this)

        val bottomSheetDialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.hubungi_via_whatsapp_bottom_sheet, null)
        val dataPenawar = intent.getParcelableExtra<GetAllNotificationResponseItem>("InfoPenawaran")

        dialogView.txtNamaPembeliWhatsApp.text = dataPenawar!!.buyer_name
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
            val message = "Hai, tawaranmu terhadap produk ${dataPenawar.Product.name} seharga " +
                    "${dataPenawar.bid_price} telah diterima oleh penjual. Jika anda " +
                    "mengkonfirmasi pembelian, silahkan kirim pesan untuk menghubungi pembeli"

            val url = "https://api.whatsapp.com/send?phone=$buyerPhone"+"&text=" + URLEncoder.encode(message, "UTF-8")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
    }

    override fun terimaButton(item: GetSellerOrderResponseItem, position: Int) {
        userLoginTokenManager = UserLoginTokenManager(this)
        val viewModelSellerOrder = ViewModelProvider(this)[SellerOrderViewModel::class.java]

        AlertDialog.Builder(this)
            .setTitle("Terima tawaran")
            .setMessage("Anda yakin ingin menerima tawaran?")
            .setNegativeButton("Batal") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("Ya") { dialogInterface: DialogInterface, _: Int ->
                userLoginTokenManager.accessToken.asLiveData().observe(this) { accessToken ->
                    viewModelSellerOrder.updateOrderStatus(
                        accessToken,
                        item.id,
                        OrderStatus("accepted")
                    )
                    viewModelSellerOrder.responseMessage.observe(this) {
                        if (it) {
                            Toast.makeText(this, "Berhasil menerima", Toast.LENGTH_SHORT).show()
                            initDialogToWhatsApp(item.Product.image_url, item.price, item.User.phone_number)
                        } else {
                            Toast.makeText(this, "Permintaan anda gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    dialogInterface.dismiss()
                }
            }.show()
    }

    override fun tolakButton(item: GetSellerOrderResponseItem, position: Int) {
        userLoginTokenManager = UserLoginTokenManager(this)
        val viewModelSellerOrder = ViewModelProvider(this)[SellerOrderViewModel::class.java]

        AlertDialog.Builder(this)
            .setTitle("Tolak tawaran")
            .setMessage("Anda yakin ingin menolak tawaran?")
            .setNegativeButton("Batal") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("Ya") { dialogInterface: DialogInterface, _: Int ->

                userLoginTokenManager.accessToken.asLiveData().observe(this) { accessToken ->
                    viewModelSellerOrder.updateOrderStatus(
                        accessToken,
                        item.id,
                        OrderStatus("declined")
                    )
                    viewModelSellerOrder.responseMessage.observe(this) {
                        if (it) {
                            Toast.makeText(this, "Berhasil menolak", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Permintaan anda gagal", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    dialogInterface.dismiss()
                }
            }.show()
    }

    override fun hubungiButton(item: GetSellerOrderResponseItem, position: Int) {
        initDialogToWhatsApp(item.Product.image_url, item.price, item.User.phone_number)
    }

    override fun statusButton(item: GetSellerOrderResponseItem, position: Int) {
        userLoginTokenManager = UserLoginTokenManager(this)
        val viewModelSellerOrder = ViewModelProvider(this)[SellerOrderViewModel::class.java]

        val dialog = BottomSheetDialog(this)
        val dialogView = layoutInflater.inflate(R.layout.status_penjualan_bottom_sheet, null)

        dialogView.btnKirimStatus.setOnClickListener {
            userLoginTokenManager.accessToken.asLiveData().observe(this) { accessToken ->
                val statusValue =
                    dialogView.findViewById<RadioButton>(dialogView.rdgStatus.checkedRadioButtonId)
                if (statusValue.text == "Berhasil terjual") {
                    viewModelSellerOrder.updateOrderStatus(
                        accessToken,
                        item.id,
                        OrderStatus("sold")
                    )
                    viewModelSellerOrder.responseMessage.observe(this) {
                        if (it) {
                            Toast.makeText(
                                this,
                                "Status produk berhasil diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Status produk gagal diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else if (statusValue.text == "Batalkan transaksi") {
                    viewModelSellerOrder.updateOrderStatus(
                        accessToken,
                        item.id,
                        OrderStatus("available")
                    )
                    viewModelSellerOrder.responseMessage.observe(this) {
                        if (it) {
                            Toast.makeText(
                                this,
                                "Status produk berhasil diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this,
                                "Status produk gagal diperbarui",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.setContentView(dialogView)
        dialog.show()
    }
}