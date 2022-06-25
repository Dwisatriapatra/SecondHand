package com.example.secondhand.view.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.reduceFileImage
import com.example.secondhand.helper.uriToFile
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.viewmodel.SellerJualProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jual.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class JualFragment : Fragment() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private var fileImage: File? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_jual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_daftar_jual_saya.isInvisible = true
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
                    startGallery()
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
        title_daftar_jual_saya.isInvisible = false
        jual_detail_produk_section.isInvisible = false
        jual_preview_button.setOnClickListener {
            //action
        }
        jual_terbitkan_button.setOnClickListener {
            //get all input
            val namaBarang =
                jual_nama_barang.text.toString().toRequestBody("multipart/form-data".toMediaType())
            val hargaBarang =
                jual_harga_barang.text.toString().toRequestBody("multipart/form-data".toMediaType())
            val lokasiBarang =
                jual_lokasi_toko.text.toString().toRequestBody("multipart/form-data".toMediaType())
            val deskripsiProduk = jual_deskripsi_produk.text.toString()
                .toRequestBody("multipart/form-data".toMediaType())

            val file = reduceFileImage(fileImage as File)
            val requestImageFile = file.asRequestBody("multipart/form-data".toMediaType())
            val imageMultiPart = MultipartBody.Part.createFormData("image", file.name, requestImageFile)


            userLoginTokenManager = UserLoginTokenManager(requireContext())
            val viewModelJualProduk =
                ViewModelProvider(this)[SellerJualProductViewModel::class.java]
            userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                viewModelJualProduk.jualProduct(
                    it,
                    hargaBarang,
                    listOf(1).toString().toRequestBody("multipart/form-data".toMediaType()),
                    deskripsiProduk,
                    imageMultiPart,
                    //fotoProdukStringBinary,
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
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if(result.resultCode == RESULT_OK){
            val selectedImg = result.data?.data as Uri
            val myImageFile = uriToFile(selectedImg, requireContext())
            fileImage = myImageFile
            jual_foto_produk.setImageURI(selectedImg)
        }
    }

}