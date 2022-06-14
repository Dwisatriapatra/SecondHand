package com.example.secondhand.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        userLoginTokenManager.accessToken.asLiveData().observe(this){
//            see_token.text = "Token: $it"
//        }
//        userLoginTokenManager.name.asLiveData().observe(this){
//            see_name.text = "Name: $it"
//        }
//        userLoginTokenManager.email.asLiveData().observe(this){
//            see_email.text = "Email: $it"
//        }

        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.user.observe(this){
            see_email.text = it.email
        }
    }
}