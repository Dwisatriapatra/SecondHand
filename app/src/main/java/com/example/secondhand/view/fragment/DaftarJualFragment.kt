package com.example.secondhand.view.fragment

import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.DaftarJualProductSayaItemClickListener
import com.example.secondhand.model.GetAllNotificationResponseItem
import com.example.secondhand.model.GetSellerOrderResponseItem
import com.example.secondhand.model.GetSellerProductItem
import com.example.secondhand.model.SellerProductUpdateRequest
import com.example.secondhand.view.activity.InfoPenawarActivity
import com.example.secondhand.view.activity.LengkapiInfoAkun
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.view.adapter.DiminatiAdapter
import com.example.secondhand.view.adapter.SellerProductAdapter
import com.example.secondhand.view.adapter.TerjualAdapter
import com.example.secondhand.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_edit_data_product_dialog_alert.view.*
import kotlinx.android.synthetic.main.fragment_daftar_jual.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class DaftarJualFragment : Fragment(), DaftarJualProductSayaItemClickListener {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private lateinit var adapter: SellerProductAdapter
    private lateinit var diminatiAdapter: DiminatiAdapter
    private lateinit var terjualAdapter: TerjualAdapter

    private var imageMultiPart: MultipartBody.Part? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var imageFile: File? = null

    private lateinit var selectedCategory: BooleanArray
    private var categoryList = arrayListOf<Int>()
    private var availableCategory = arrayOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_jual, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelSeller = ViewModelProvider(this)[SellerViewModel::class.java]

        userLoginTokenManager = UserLoginTokenManager(requireContext())

        swipe_refresh_daftar_jual.setOnRefreshListener {
            swipe_refresh_daftar_jual.isRefreshing = false
            refreshCurrentFragment()
        }

        userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner) { isUser ->
            if (isUser) {
                daftar_jual_saya_belum_login_section.isInvisible = true
                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                    viewModelSeller.getSellerData(it)
                }

                initView()
            } else {
                daftar_jual_saya_title.isInvisible = true
                daftar_jual_saya_profile_penjual_section.isInvisible = true
                daftar_jual_saya_filter_produk_section.isInvisible = true
                rv_daftar_jual_saya_produk.isInvisible = true
                daftar_jual_saya_progress_bar.isInvisible = true
                daftar_jual_saya_no_data_animation.isInvisible = true

                daftar_jual_saya_to_login.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }


            }
        }

    }

    private fun initView() {
        val viewModelSeller = ViewModelProvider(this)[SellerViewModel::class.java]
        daftar_jual_saya_edit_profile_penjual_button.setOnClickListener {
            startActivity(Intent(activity, LengkapiInfoAkun::class.java))
        }
        viewModelSeller.seller.observe(viewLifecycleOwner) {
            daftar_jual_saya_nama_penjual.text = it.full_name
            daftar_jual_saya_kota_penjual.text = it.city
            Glide.with(daftar_jual_saya_image_penjual.context)
                .load(it.image_url)
                .error(R.drawable.ic_launcher_background)
                .override(72, 72)
                .into(daftar_jual_saya_image_penjual)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        userLoginTokenManager = UserLoginTokenManager(requireContext())
        val viewModelSellerProduct = ViewModelProvider(this)[SellerProductViewModel::class.java]
        val viewModelNotification = ViewModelProvider(this)[NotificationViewModel::class.java]
        val viewModelSellerOrder = ViewModelProvider(this)[SellerOrderViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
            viewModelSellerProduct.getAllSellerProduct(it)
            viewModelNotification.getAllNotification(it)
            viewModelSellerOrder.getAllSellerOrder(it)
        }

        adapter = SellerProductAdapter(this@DaftarJualFragment)
        rv_daftar_jual_saya_produk.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_daftar_jual_saya_produk.adapter = adapter

        diminatiAdapter = DiminatiAdapter {
            val intent = Intent(activity, InfoPenawarActivity::class.java)
            intent.putExtra("InfoPenawaran", it)
            startActivity(intent)
        }
        rv_daftar_jual_saya_diminati.layoutManager = LinearLayoutManager(requireContext())
        rv_daftar_jual_saya_diminati.adapter = diminatiAdapter

        terjualAdapter = TerjualAdapter()
        rv_daftar_jual_saya_terjual.layoutManager = LinearLayoutManager(requireContext())
        rv_daftar_jual_saya_terjual.adapter = terjualAdapter


        // init on "semua produk" category
        daftar_jual_saya_filter_produk_product.isSelected = true
        viewModelSellerProduct.sellerProduct.observe(viewLifecycleOwner) { product ->
            val listProdukTersedia = mutableListOf<GetSellerProductItem>()
            if (product.isNotEmpty()) {
                for (i in product.indices) {
                    if (product[i].status == "available") {
                        listProdukTersedia += product[i]
                    }
                }
                if (listProdukTersedia.isEmpty()) {
                    daftar_jual_saya_no_data_animation.isInvisible = false
                    daftar_jual_saya_progress_bar.isInvisible = true
                } else {
                    adapter.setDataSellerProduct(listProdukTersedia)
                    daftar_jual_saya_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                    daftar_jual_saya_no_data_animation.isInvisible = true
                }
            } else {
                daftar_jual_saya_progress_bar.isInvisible = true
                daftar_jual_saya_no_data_animation.isInvisible = false
            }
        }

        daftar_jual_saya_filter_produk_product.setOnClickListener {
            daftar_jual_saya_filter_produk_diminati.isSelected = false
            daftar_jual_saya_filter_produk_product.isSelected = true
            daftar_jual_saya_filter_produk_terjual.isSelected = false

            rv_daftar_jual_saya_diminati.isInvisible = true
            rv_daftar_jual_saya_terjual.isInvisible = true
            rv_daftar_jual_saya_produk.isInvisible = false

            daftar_jual_saya_progress_bar.isInvisible = false

            viewModelSellerProduct.sellerProduct.observe(viewLifecycleOwner) { product ->
                val listProdukTersedia = mutableListOf<GetSellerProductItem>()
                if (product.isNotEmpty()) {
                    for (i in product.indices) {
                        if (product[i].status == "available") {
                            listProdukTersedia += product[i]
                        }
                    }
                    if (listProdukTersedia.isEmpty()) {
                        daftar_jual_saya_no_data_animation.isInvisible = false
                        daftar_jual_saya_progress_bar.isInvisible = true
                    } else {
                        adapter.setDataSellerProduct(listProdukTersedia)
                        daftar_jual_saya_progress_bar.isInvisible = true
                        adapter.notifyDataSetChanged()
                        daftar_jual_saya_no_data_animation.isInvisible = true
                    }
                } else {
                    daftar_jual_saya_progress_bar.isInvisible = true
                    daftar_jual_saya_no_data_animation.isInvisible = false
                }
            }
        }

        daftar_jual_saya_filter_produk_diminati.setOnClickListener {
            daftar_jual_saya_filter_produk_diminati.isSelected = true
            daftar_jual_saya_filter_produk_product.isSelected = false
            daftar_jual_saya_filter_produk_terjual.isSelected = false

            rv_daftar_jual_saya_diminati.isInvisible = false
            rv_daftar_jual_saya_produk.isInvisible = true
            rv_daftar_jual_saya_terjual.isInvisible = true

            daftar_jual_saya_progress_bar.isInvisible = false


            viewModelNotification.notification.observe(viewLifecycleOwner) { allNotification ->
                val listProdukDiminati = mutableListOf<GetAllNotificationResponseItem>()
                if (allNotification.isNotEmpty()) {
                    for (i in allNotification.indices) {
                        if (allNotification[i].status == "bid" && allNotification[i].Product != null) {
                            listProdukDiminati += allNotification[i]
                        }
                    }
                    if (listProdukDiminati.isEmpty()) {
                        daftar_jual_saya_no_data_animation.isInvisible = false
                        daftar_jual_saya_progress_bar.isInvisible = true
                    } else {
                        diminatiAdapter.setDiminatiData(listProdukDiminati)
                        diminatiAdapter.notifyDataSetChanged()
                        daftar_jual_saya_progress_bar.isInvisible = true
                        daftar_jual_saya_no_data_animation.isInvisible = true
                    }
                } else {
                    daftar_jual_saya_progress_bar.isInvisible = true
                    daftar_jual_saya_no_data_animation.isInvisible = false
                }
            }
        }

        daftar_jual_saya_filter_produk_terjual.setOnClickListener {
            daftar_jual_saya_filter_produk_diminati.isSelected = false
            daftar_jual_saya_filter_produk_product.isSelected = false
            daftar_jual_saya_filter_produk_terjual.isSelected = true

            rv_daftar_jual_saya_diminati.isInvisible = true
            rv_daftar_jual_saya_produk.isInvisible = true
            rv_daftar_jual_saya_terjual.isInvisible = false

            daftar_jual_saya_progress_bar.isInvisible = false

            viewModelSellerOrder.sellerOrder.observe(viewLifecycleOwner) { allOrder ->
                val listProductTerjual = mutableListOf<GetSellerOrderResponseItem>()
                if (allOrder.isNotEmpty()) {
                    for (i in allOrder.indices) {
                        if (allOrder[i].Product.status == "sold") {
                            listProductTerjual += allOrder[i]
                        }
                    }
                    if (listProductTerjual.isEmpty()) {
                        daftar_jual_saya_no_data_animation.isInvisible = false
                        daftar_jual_saya_progress_bar.isInvisible = true
                    } else {
                        terjualAdapter.setListProdukTerjual(listProductTerjual)
                        terjualAdapter.notifyDataSetChanged()
                        daftar_jual_saya_no_data_animation.isInvisible = true
                        daftar_jual_saya_progress_bar.isInvisible = true
                    }
                } else {
                    daftar_jual_saya_progress_bar.isInvisible = true
                    daftar_jual_saya_no_data_animation.isInvisible = false
                }
            }
        }
    }

    override fun editProductInDaftarJualSaya(
        item: GetSellerProductItem,
        position: Int
    ) {
        userLoginTokenManager = UserLoginTokenManager(requireContext())
        val viewModelSellerProduct = ViewModelProvider(this)[SellerProductViewModel::class.java]

        val viewModelCategory = ViewModelProvider(this)[CategoryViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
            viewModelCategory.getAllProductCategoryAvailable()
        }
        viewModelCategory.listCategory.observe(viewLifecycleOwner) {
            val availCategory = emptyList<String>().toMutableList()
            for (i in it.indices) {
                availCategory += it[i].name
            }
            availableCategory = availCategory.toTypedArray()
        }

        val customDialogEdit = LayoutInflater.from(requireContext()).inflate(
            R.layout.custom_edit_data_product_dialog_alert, null, false
        )
        val editDataDialog = AlertDialog.Builder(requireContext())
            .setView(customDialogEdit)
            .create()

        // initializing
        customDialogEdit.edit_product_nama.setText(item.name)
        customDialogEdit.edit_product_deskripsi.setText(item.description)
        customDialogEdit.edit_product_harga.setText(item.base_price.toString())
        customDialogEdit.edit_product_lokasi_toko.setText(item.location)
        Glide.with(customDialogEdit.edit_product_foto.context)
            .load(item.image_url)
            .error(R.drawable.ic_launcher_background)
            .into(customDialogEdit.edit_product_foto)
        customDialogEdit.edit_product_kategori.text = ""
        if (item.Categories.isNotEmpty()) {
            for (i in item.Categories.indices) {
                if (item.Categories.lastIndex == 0) {
                    customDialogEdit.edit_product_kategori.text = item.Categories[i].name
                    break
                }
                if (i == 0) {
                    customDialogEdit.edit_product_kategori.text = "${item.Categories[i].name}, "
                } else if (i != item.Categories.lastIndex && i > 0) {
                    customDialogEdit.edit_product_kategori.text =
                        customDialogEdit.edit_product_kategori.text.toString() + item.Categories[i].name + ", "
                } else {
                    customDialogEdit.edit_product_kategori.text =
                        customDialogEdit.edit_product_kategori.text.toString() + item.Categories[i].name
                }
            }
        } else {
            customDialogEdit.edit_product_kategori.text = "Kategori: Belum ada kategori"
        }


        customDialogEdit.edit_product_kategori.setOnClickListener {
            initMultiChoicesAlertDialog(customDialogEdit)
        }

        customDialogEdit.edit_product_foto_button.setOnClickListener {
            openGallery()
            customDialogEdit.edit_product_foto.setImageURI(imageUri)
        }

        customDialogEdit.edit_product_update_button.setOnClickListener {
            val namaBaruProduct = customDialogEdit.edit_product_nama.text.toString()
                .toRequestBody("multipart/form-data".toMediaType())
            val hargaBaruProduct =
                customDialogEdit.edit_product_harga.getNumericValue().toInt().toString()
                    .toRequestBody("multipart/form-data".toMediaType())
            val lokasiBaruToko = customDialogEdit.edit_product_lokasi_toko.text.toString()
                .toRequestBody("multipart/form-data".toMediaType())
            val deskripsiBaruProduct = customDialogEdit.edit_product_deskripsi.text.toString()
                .toRequestBody("multipart/form-data".toMediaType())

            val kategoriList = ArrayList<MultipartBody.Part>()
            if (categoryList.isNotEmpty()) {
                for (i in categoryList.indices) {
                    kategoriList.add(
                        MultipartBody.Part.createFormData(
                            "category_ids",
                            (categoryList[i] + 1).toString()
                        )
                    )
                }
            } else {
                if (item.Categories.isNotEmpty()) {
                    for (i in item.Categories.indices) {
                        kategoriList.add(
                            MultipartBody.Part.createFormData(
                                "category_ids",
                                item.Categories[i].id.toString()
                            )
                        )
                    }
                } else {
                    //nothing to do
                }
            }

            if (
//                customDialogEdit.edit_product_harga.text.toString().isNotEmpty() &&
//                customDialogEdit.edit_product_kategori.text.toString().isNotEmpty() &&
//                customDialogEdit.edit_product_lokasi_toko.text.toString().isNotEmpty() &&
//                customDialogEdit.edit_product_deskripsi.text.toString().isNotEmpty() &&
//                customDialogEdit.edit_product_nama.text.toString().isNotEmpty() &&
                imageFile != null
            ) {
                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                    val file = (imageFile as File)
                    val requestImageFile = file.asRequestBody("multipart/form-data".toMediaType())
                    val imageMultipart =
                        MultipartBody.Part.createFormData("image", file.name, requestImageFile)
                    viewModelSellerProduct.updateProductInDaftarJualSaya(
                        it,
                        item.id,
                        SellerProductUpdateRequest(
                            hargaBaruProduct,
                            kategoriList,
                            deskripsiBaruProduct,
                            imageMultipart,
                            lokasiBaruToko,
                            namaBaruProduct
                        )
                    )
                    viewModelSellerProduct.getAllSellerProduct(it)
                    viewModelSellerProduct.sellerProduct.observe(viewLifecycleOwner) { sellerProduct ->
                        adapter.setDataSellerProduct(sellerProduct)
                        adapter.notifyDataSetChanged()
                    }
                    viewModelSellerProduct.responseMessage.observe(viewLifecycleOwner) { responseMsg ->
                        if (responseMsg) {
                            Toast.makeText(
                                requireContext(),
                                "Berhasil diupdate",
                                Toast.LENGTH_SHORT
                            ).show()
                            editDataDialog.dismiss()
                            refreshCurrentFragment()
                        } else {
                            Toast.makeText(requireContext(), "Gagal diupdate", Toast.LENGTH_SHORT)
                                .show()
                            editDataDialog.dismiss()
                            refreshCurrentFragment()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Foto belum diperbaharui", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        editDataDialog.show()
    }

    override fun deleteProductFromDaftarJualSaya(item: GetSellerProductItem, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Produk")
            .setMessage("Anda yakin ingin menghapus produk ini?")
            .setNegativeButton("Batal") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("Ya") { _: DialogInterface, _: Int ->
                userLoginTokenManager = UserLoginTokenManager(requireContext())
                val viewModelSellerProduct =
                    ViewModelProvider(this)[SellerProductViewModel::class.java]
                userLoginTokenManager.accessToken.asLiveData()
                    .observe(viewLifecycleOwner) { accessToken ->
                        viewModelSellerProduct.deleteProductFromDaftarJualSaya(accessToken, item.id)
                    }
                adapter.deleteSellerProductByPosition(position)
                adapter.notifyItemRemoved(position)
            }.show()

    }

    private fun initMultiChoicesAlertDialog(view: View) {
        selectedCategory = BooleanArray(availableCategory.size)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Pilih kategori (maks. 4)")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMultiChoiceItems(
            availableCategory, selectedCategory
        ) { _: DialogInterface, i: Int, b: Boolean ->
            if (b) {
                if (categoryList.isEmpty()) {
                    categoryList.add(i)
                } else {
                    if (categoryList.size >= 4) {
                        Toast.makeText(requireContext(), "Maksimal 4 kategori", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        categoryList.add(i)
                    }
                }
            } else {
                categoryList.remove(Integer.valueOf(i))
            }
        }
        alertDialogBuilder.setPositiveButton(
            "OK"
        ) { _, _ ->
            val stringBuilder = StringBuilder()
            for (j in 0 until categoryList.size) {
                stringBuilder.append(availableCategory[categoryList[j]])
                if (j != categoryList.size - 1) {
                    stringBuilder.append(", ")
                }
            }
            view.edit_product_kategori.text = stringBuilder.toString()
        }

        alertDialogBuilder.setNegativeButton(
            "Cancel"
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNeutralButton(
            "Clear All"
        ) { _, _ ->
            for (j in selectedCategory.indices) {
                selectedCategory[j] = false
                categoryList.clear()
                view.edit_product_kategori.text = ""
            }
        }
        alertDialogBuilder.show()
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = context!!.contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                val tempFile = File.createTempFile("temp-", ".jpg", null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart =
                    MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }

    private fun refreshCurrentFragment() {
        val id = findNavController().currentDestination!!.id
        findNavController().popBackStack(id, true)
        findNavController().navigate(id)
    }
}