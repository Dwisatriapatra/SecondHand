package com.example.secondhand.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.isOnline
import com.example.secondhand.model.LoginRequestUser
import com.example.secondhand.model.LoginResponsePostUser
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SplashAcivity : AppCompatActivity() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_acivity)

        userLoginTokenManager = UserLoginTokenManager(this)

        Handler(Looper.getMainLooper()).postDelayed({

            if (isOnline(this)) {
                userLoginTokenManager.isUser.asLiveData().observe(this) { isUser ->
                    if (isUser) {
                        userLoginTokenManager.booelan.asLiveData().observe(this) {
                            var email = ""
                            var password: String
                            if (it == true) {

                                userLoginTokenManager.email.asLiveData().observe(this) { result ->
                                    email = result.toString()
                                }
                                userLoginTokenManager.password.asLiveData()
                                    .observe(this) { result ->
                                        password = result.toString()
                                        requestNewLoginToken(email, password)
                                    }

                                //request new token

                            } else {
                                startActivity(Intent(this, LoginActivity::class.java))
                            }
                        }
                    } else {
                        Toast.makeText(this, "You are login as guest", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            } else {
                Toast.makeText(this, "You are offline", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }, 6000)

    }

    private fun requestNewLoginToken(email: String, password: String) {
        val viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        viewModelUser.userLogin(LoginRequestUser(email, password))
        viewModelUser.responseMessage.observe(this) { responseMessage ->
            if (responseMessage!!) {
                viewModelUser.user.observe(this) {
                    saveToken(it, password)
                }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveToken(loginResponsePostUser: LoginResponsePostUser, password: String) {
        userLoginTokenManager = UserLoginTokenManager(this)
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                userLoginTokenManager.setBoolean(true)
                userLoginTokenManager.saveToken(
                    loginResponsePostUser.email,
                    loginResponsePostUser.name,
                    loginResponsePostUser.access_token,
                    password
                )
                startActivity(Intent(this@SplashAcivity, MainActivity::class.java))
            }
        }
    }
}