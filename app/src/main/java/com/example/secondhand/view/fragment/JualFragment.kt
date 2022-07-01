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
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.PostJualProduct
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.view.activity.PreviewActivity
import com.example.secondhand.viewmodel.SellerJualProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jual.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class JualFragment : Fragment() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private var imageMultiPart: MultipartBody.Part? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var imageFile: File? = null
    private lateinit var selectedCategory: BooleanArray
    private var categoryList = arrayListOf<Int>()
    private var availableCategory = arrayOf(
        "Makanan",
        "Minuman",
        "Fashion",
        "Alat dapur",
        "Kesehatan",
        "Olahraga",
        "Hobi",
        "Kendaraan",
        "Lainnya"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_lengkapi_detail_produk.isInvisible = true
        jual_detail_produk_section.isInvisible = true

        userLoginTokenManager = UserLoginTokenManager(requireContext())
        userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner) { isUser ->
            if (isUser) {

                jual_belum_login_section.isInvisible = true

                jual_jual_produk_baru_button.setOnClickListener {
                    jual_jual_produk_baru_button.isInvisible = true
                    initView()
                }

                jual_foto_produk.setOnClickListener {
                    openGallery()
                }


            } else {
                jual_jual_produk_baru_button.isInvisible = true
                jual_to_login_button.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }


    }

    private fun initView() {
        title_lengkapi_detail_produk.isInvisible = false
        jual_detail_produk_section.isInvisible = false

        jual_kategori_barang.setOnClickListener {
            initMultiChoicesAlertDialog()
        }

        jual_preview_button.setOnClickListener {
            val namaBarang =
                jual_nama_barang.text.toString()
            val hargaBarang =
                jual_harga_barang.text.toString()
            val lokasiToko =
                jual_lokasi_toko.text.toString()
            val deskripsiProduk = jual_deskripsi_produk.text.toString()
            val kategori = "kategorinya"

            if (namaBarang.isNotEmpty() &&
                hargaBarang.isNotEmpty() &&
                lokasiToko.isNotEmpty() &&
                deskripsiProduk.isNotEmpty() &&
                imageUri != Uri.EMPTY
            ) {
                val intent = Intent(activity, PreviewActivity::class.java)
                intent.putExtra(
                    "dataprodukjual",
                    PostJualProduct(
                        namaBarang,
                        hargaBarang.toInt(),
                        lokasiToko,
                        deskripsiProduk,
                        imageUri!!.toString(),
                        kategori,
                        imageFile!!
                    )
                )
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        jual_terbitkan_button.setOnClickListener {
            if (jual_nama_barang.text.toString().isNotEmpty() &&
                jual_harga_barang.text.toString().isNotEmpty() &&
                jual_lokasi_toko.text.toString().isNotEmpty() &&
                jual_deskripsi_produk.text.toString().isNotEmpty() &&
                imageMultiPart != null
            ) {

                //get all input
                val namaBarang =
                    jual_nama_barang.text.toString()
                        .toRequestBody("multipart/form-data".toMediaType())
                val hargaBarang =
                    jual_harga_barang.text.toString()
                        .toRequestBody("multipart/form-data".toMediaType())
                val lokasiBarang =
                    jual_lokasi_toko.text.toString()
                        .toRequestBody("multipart/form-data".toMediaType())
                val deskripsiProduk = jual_deskripsi_produk.text.toString()
                    .toRequestBody("multipart/form-data".toMediaType())

                userLoginTokenManager = UserLoginTokenManager(requireContext())
                val viewModelJualProduk =
                    ViewModelProvider(this)[SellerJualProductViewModel::class.java]

                val kategoriList = ArrayList<MultipartBody.Part>()

                if(categoryList.isNotEmpty()){
                    for(i in categoryList.indices){
                        kategoriList.add(MultipartBody.Part.createFormData("category_ids", categoryList[i].toString()))
                    }
                }

                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                    viewModelJualProduk.jualProduct(
                        it,
                        hargaBarang,
                        kategoriList,
                        deskripsiProduk,
                        imageMultiPart!!,
                        lokasiBarang,
                        namaBarang
                    )
                    viewModelJualProduk.responseMessage.observe(viewLifecycleOwner) { responseMsg ->
                        if (responseMsg == true) {
                            Toast.makeText(
                                requireContext(),
                                "Barang berhasil dijual",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openGallery(){
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = context!!.contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                jual_foto_produk.setImageURI(it)

                val tempFile = File.createTempFile("temp-", null, null)
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

    private fun initMultiChoicesAlertDialog(){
        selectedCategory = BooleanArray(availableCategory.size)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Pilih kategori (maks. 4)")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMultiChoiceItems(availableCategory, selectedCategory
        ) { _: DialogInterface, i: Int, b: Boolean ->
            if (b) {
                if(categoryList.isEmpty()){
                    categoryList.add(i)
                }else{
                    if(categoryList.size >= 4){
                        Toast.makeText(requireContext(), "Maksimal 4 kategori", Toast.LENGTH_SHORT).show()
                    }else{
                        categoryList.add(i)
                    }
                }
            } else {
                categoryList.remove(Integer.valueOf(i))
            }
        }
        alertDialogBuilder.setPositiveButton("OK"
        ) { _, _ -> // Initialize string builder
            val stringBuilder = StringBuilder()
            for (j in 0 until categoryList.size) {
                stringBuilder.append(availableCategory[categoryList[j]])
                if (j != categoryList.size - 1) {
                    stringBuilder.append(", ")
                }
            }
            jual_kategori_barang.text = stringBuilder.toString()
        }

        alertDialogBuilder.setNegativeButton("Cancel"
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNeutralButton("Clear All"
        ) { _, _ ->
            // use for loop
            for (j in selectedCategory.indices) {
                selectedCategory[j] = false
                categoryList.clear()
                jual_kategori_barang.text = ""
            }
        }
        alertDialogBuilder.show()
    }
}