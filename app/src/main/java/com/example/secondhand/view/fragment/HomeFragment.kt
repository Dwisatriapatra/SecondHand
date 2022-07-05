package com.example.secondhand.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.model.GetBuyerProductResponseItem
import com.example.secondhand.view.activity.DetailActivity
import com.example.secondhand.view.adapter.BuyerProductAdapter
import com.example.secondhand.view.adapter.SearchResultAdapter
import com.example.secondhand.viewmodel.BuyerProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var adapter: BuyerProductAdapter
    private lateinit var searchProductResultAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val viewModelBuyerProduct = ViewModelProvider(this)[BuyerProductViewModel::class.java]
        viewModelBuyerProduct.getAllBuyerProduct()
        adapter = BuyerProductAdapter {
            val pindah = Intent(activity, DetailActivity::class.java)
            pindah.putExtra("detailbarang", it)
            startActivity(pindah)
        }
        rv_product_home.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_product_home.adapter = adapter


        // initialize adapter and layout manager for searching activity
        searchProductResultAdapter = SearchResultAdapter {
            val pindah = Intent(activity, DetailActivity::class.java)
            pindah.putExtra("detailbarangsearchresult", it)
            startActivity(pindah)
        }
        home_list_search_result.layoutManager = LinearLayoutManager(requireContext())
        home_list_search_result.adapter = searchProductResultAdapter


        // prevent user to click these button while load data from server
        home_telusuri_kategori_elektronik_button.isClickable = false
        home_telusuri_kategori_semua_button.isClickable = false
        home_telusuri_kategori_semua_button.isClickable = false

        home_telusuri_kategori_semua_button.isSelected = true
        viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.setDataBuyerProduct(it)
                rv_product_home_progress_bar.isInvisible = true
                adapter.notifyDataSetChanged()

                //activate "telusuri kategori" button
                home_telusuri_kategori_elektronik_button.isClickable = true
                home_telusuri_kategori_semua_button.isClickable = true
            }
        }


        home_telusuri_kategori_semua_button.setOnClickListener {
            //button state
            home_telusuri_kategori_semua_button.isSelected = true
            home_telusuri_kategori_elektronik_button.isSelected = false
            home_telusuri_kategori_lainnya_button.isSelected = false
            home_telusuri_kategori_hobbi_button.isSelected = false
            home_telusuri_kategori_kendaraan_button.isSelected = false

            viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    adapter.setDataBuyerProduct(it)
                    rv_product_home_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            }
        }


        home_telusuri_kategori_elektronik_button.setOnClickListener {
            home_telusuri_kategori_elektronik_button.isSelected = true
            home_telusuri_kategori_semua_button.isSelected = false
            home_telusuri_kategori_lainnya_button.isSelected = false
            home_telusuri_kategori_hobbi_button.isSelected = false
            home_telusuri_kategori_kendaraan_button.isSelected = false

            viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner) {
                val listProduct: MutableList<GetBuyerProductResponseItem> = mutableListOf()
                if (it.isNotEmpty()) {
                    for (i in it.indices) {
                        for (j in it[i].Categories!!.indices) {
                            if (it[i].Categories!![j].name == "Electronic") {
                                listProduct += it[i]
                            }
                        }
                    }
                    adapter.setDataBuyerProduct(listProduct)
                    rv_product_home_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            }
        }

        home_telusuri_kategori_lainnya_button.setOnClickListener {
            home_telusuri_kategori_lainnya_button.isSelected = true
            home_telusuri_kategori_elektronik_button.isSelected = false
            home_telusuri_kategori_semua_button.isSelected = false
            home_telusuri_kategori_hobbi_button.isSelected = false
            home_telusuri_kategori_kendaraan_button.isSelected = false

            viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner) {
                val listProduct: MutableList<GetBuyerProductResponseItem> = mutableListOf()
                if (it.isNotEmpty()) {
                    for (i in it.indices) {
                        if (it[i].Categories!!.isNotEmpty()) {
                            for (j in it[i].Categories!!.indices) {
                                val name = it[i].Categories!![j].name
                                if (name != "Electronic" && name != "Minuman" && name != "kendaraan" && name != "hobbi") {
                                    listProduct += it[i]
                                }
                            }
                        }

                    }
                    adapter.setDataBuyerProduct(listProduct)
                    rv_product_home_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            }
        }

        home_telusuri_kategori_kendaraan_button.setOnClickListener {
            home_telusuri_kategori_lainnya_button.isSelected = false
            home_telusuri_kategori_elektronik_button.isSelected = false
            home_telusuri_kategori_semua_button.isSelected = false
            home_telusuri_kategori_hobbi_button.isSelected = false
            home_telusuri_kategori_kendaraan_button.isSelected = true

            viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner) {
                val listProduct: MutableList<GetBuyerProductResponseItem> = mutableListOf()
                if (it.isNotEmpty()) {
                    for (i in it.indices) {
                        if (it[i].Categories!!.isNotEmpty()) {
                            for (j in it[i].Categories!!.indices) {
                                val name = it[i].Categories!![j].name
                                if (name == "kendaraan") {
                                    listProduct += it[i]
                                }
                            }
                        }

                    }
                    adapter.setDataBuyerProduct(listProduct)
                    rv_product_home_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            }
        }

        home_telusuri_kategori_hobbi_button.setOnClickListener {
            home_telusuri_kategori_lainnya_button.isSelected = false
            home_telusuri_kategori_elektronik_button.isSelected = false
            home_telusuri_kategori_semua_button.isSelected = false
            home_telusuri_kategori_hobbi_button.isSelected = true
            home_telusuri_kategori_kendaraan_button.isSelected = false

            viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner) {
                val listProduct: MutableList<GetBuyerProductResponseItem> = mutableListOf()
                if (it.isNotEmpty()) {
                    for (i in it.indices) {
                        if (it[i].Categories!!.isNotEmpty()) {
                            for (j in it[i].Categories!!.indices) {
                                val name = it[i].Categories!![j].name
                                if (name == "hobbi") {
                                    listProduct += it[i]
                                }
                            }
                        }

                    }
                    adapter.setDataBuyerProduct(listProduct)
                    rv_product_home_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            }
        }

        home_list_search_result.isInvisible = true
        home_search_bar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            val viewModelSearch = ViewModelProvider(this@HomeFragment)[BuyerProductViewModel::class.java]

            override fun onQueryTextSubmit(query: String?): Boolean {

                home_banner_section.isInvisible = true
                home_telusuri_kategori_section.isInvisible = true
                rv_product_home_section.isInvisible = true
                home_list_search_result.isInvisible = false

                home_search_bar.clearFocus()

                viewModelSearch.getBuyerProductSearchResult(query!!)

                viewModelSearch.searchResult.observe(viewLifecycleOwner){value ->
                    searchProductResultAdapter.setDataBuyerProductSearch(value)
                    searchProductResultAdapter.notifyDataSetChanged()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }


}