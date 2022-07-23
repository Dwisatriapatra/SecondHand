package com.example.secondhand.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.view.adapter.HistoryAdapter
import com.example.secondhand.viewmodel.HistoryViewModel
import com.example.secondhand.viewmodel.SellerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_history.*

@AndroidEntryPoint
class HistoryActivity : AppCompatActivity() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        iniView()
    }

    private fun iniView() {
        val historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        val sellerViewModel = ViewModelProvider(this)[SellerViewModel::class.java]
        userLoginTokenManager = UserLoginTokenManager(this)

        adapter = HistoryAdapter()
        rv_history.layoutManager = LinearLayoutManager(this)
        rv_history.adapter = adapter

        userLoginTokenManager.accessToken.asLiveData().observe(this) {
            historyViewModel.getAllUserHistory(it)
            sellerViewModel.getSellerData(it)
        }

        sellerViewModel.seller.observe(this) { sellerData ->
            if (sellerData != null) {
                history_nama_user.text = sellerData.full_name + "\'s History"
            }
        }

        historyViewModel.history.observe(this) {
            if (it.isNotEmpty()) {
                adapter.setDataUserHistory(it)
                adapter.notifyDataSetChanged()
            }
        }
    }
}