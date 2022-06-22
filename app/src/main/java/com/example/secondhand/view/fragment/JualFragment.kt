package com.example.secondhand.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.RequestJualProduct
import com.example.secondhand.viewmodel.SellerJualProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_jual.*

@AndroidEntryPoint
class JualFragment : Fragment() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_daftar_jual_saya.isInvisible = true
        jual_detail_produk_section.isInvisible = true
        jual_jual_produk_baru_button.setOnClickListener {
            jual_jual_produk_baru_button.isInvisible = true
            initView()
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
            val namaBarang = jual_nama_barang.text.toString()
            val hargaBarang = jual_harga_barang.text.toString()
            //val kategoriBarang = jual_kategori_barang.text.toString()
            val lokasiBarang = jual_lokasi_toko.text.toString()
            val deskripsiProduk = jual_deskripsi_produk.text.toString()

            userLoginTokenManager = UserLoginTokenManager(requireContext())
            val viewModelJualProduk = ViewModelProvider(this)[SellerJualProductViewModel::class.java]
            userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                viewModelJualProduk.jualProduct(
                    it,
                    RequestJualProduct(
                        hargaBarang.toInt(),
                        listOf(1),
                        deskripsiProduk,
                        "string",
                        lokasiBarang,
                        namaBarang
                    )
                )
                viewModelJualProduk.responseMessage.observe(viewLifecycleOwner){responseMsg ->
                    if(responseMsg == true){
                        Toast.makeText(requireContext(), "Barang berhasil dijual", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}