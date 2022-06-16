package com.example.secondhand.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.view.DetailActivity
import com.example.secondhand.view.adapter.BuyerProductAdapter
import com.example.secondhand.viewmodel.BuyerProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var adapter: BuyerProductAdapter
    private lateinit var userLoginTokenManager: UserLoginTokenManager

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
        userLoginTokenManager = UserLoginTokenManager(requireContext())
        val viewModelBuyerProduct = ViewModelProvider(this)[BuyerProductViewModel::class.java]
        userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner){
            viewModelBuyerProduct.getAllBuyerProduct(it)
        }



        adapter = BuyerProductAdapter(){
            val pindah = Intent(activity, DetailActivity::class.java)
            pindah.putExtra("detailbarang", it)
            startActivity(pindah)

//            val pindah = Bundle()
//            pindah.putParcelable("detailbarang", it)
//            view!!.findNavController().navigate(R.id.homeKe_detailFragment)

            //do something
            //edit
        }
        rv_product_home.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_product_home.adapter = adapter


        viewModelBuyerProduct.buyerProduct.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                adapter.setDataBuyerProduct(it)
                adapter.notifyDataSetChanged()
            }
        }
    }


}