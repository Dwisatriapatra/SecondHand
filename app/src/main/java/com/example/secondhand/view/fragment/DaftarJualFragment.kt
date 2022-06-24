package com.example.secondhand.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.view.adapter.SellerProductAdapter
import com.example.secondhand.viewmodel.SellerProductViewModel
import com.example.secondhand.viewmodel.SellerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_daftar_jual.*

@AndroidEntryPoint
class DaftarJualFragment : Fragment() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private lateinit var adapter: SellerProductAdapter

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

        userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner){isUser ->
            if(isUser){
                daftar_jual_saya_belum_login_section.isInvisible = true
                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner){
                    viewModelSeller.getSeller(it)
                }

                initView()
            }else{
                daftar_jual_saya_title.isInvisible = true
                daftar_jual_saya_profile_penjual_section.isInvisible = true
                daftar_jual_saya_filter_produk_section.isInvisible = true
                rv_daftar_jual_saya.isInvisible = true
                daftar_jual_saya_progress_bar.isInvisible = true

                daftar_jual_saya_to_login.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }


            }
        }




    }

    private fun initView() {
        val viewModelSeller = ViewModelProvider(this)[SellerViewModel::class.java]
        viewModelSeller.seller.observe(viewLifecycleOwner){
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

    private fun initRecyclerView(){
        userLoginTokenManager = UserLoginTokenManager(requireContext())
        val viewModelSellerProduct = ViewModelProvider(this)[SellerProductViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner){
            viewModelSellerProduct.getAllSellerProduct(it)
        }
        adapter = SellerProductAdapter {
            //do something
            //edit
        }
        rv_daftar_jual_saya.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_daftar_jual_saya.adapter = adapter

        viewModelSellerProduct.sellerProduct.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                adapter.setDataSellerProduct(it)
                daftar_jual_saya_progress_bar.isInvisible = true
                adapter.notifyDataSetChanged()
            }
        }

    }
}