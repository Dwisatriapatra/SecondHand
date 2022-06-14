package com.example.secondhand.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userLoginTokenManager = UserLoginTokenManager(this)

        userLoginTokenManager.booelan.asLiveData().observe(this){
            if(it == true){
                startActivity(Intent(this, MainActivity::class.java))
            }else{
                button_login.setOnClickListener {
                    login()
                }
            }
        }

        login_to_register_akun.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))


            //lanjutkan untuk mekanisme register
        }

    }

    private fun login(){
        val viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        val email = login_input_email.text.toString()
        val password = login_input_password.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.Main).launch {
                viewModelUser.userLogin(LoginRequestUser(email, password)).also {
                    GlobalScope.launch {
                        userLoginTokenManager.setBoolean(true)
                    }
                    Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                    saveToken()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }

        }else{
            Toast.makeText(this, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToken() {
        val viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        viewModelUser.user.observe(this){
            GlobalScope.launch {
                userLoginTokenManager.saveToken(it.email, it.name, it.access_token)
            }
        }
    }
}