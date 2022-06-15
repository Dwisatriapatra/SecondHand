package com.example.secondhand.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userLoginTokenManager = UserLoginTokenManager(this)
        userLoginTokenManager.accessToken.asLiveData().observe(this){
            result.text = it
            Log.d("cekkkkkk","token: $it")
        }
    }
}