package com.example.secondhand.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
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
    private lateinit var email : String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //enable full screen
        setContentView(R.layout.activity_splash_acivity)

        userLoginTokenManager = UserLoginTokenManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            userLoginTokenManager.booelan.asLiveData().observe(this) {
                if (it == true) {

                    userLoginTokenManager.email.asLiveData().observe(this){result ->
                        email = result.toString()
                    }
                    userLoginTokenManager.password.asLiveData().observe(this){result ->
                        password = result.toString()
                    }

                    //request new token
                    requestNewLoginToken(email, password)
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }, 4000)

    }

    private fun requestNewLoginToken(email: String, password: String) {
        val viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        viewModelUser.userLogin(LoginRequestUser(email, password))
        viewModelUser.responseMessage.observe(this){responseMessage ->
            if(responseMessage){
                viewModelUser.user.observe(this){
                    saveToken(it, password)
                }
            }else{
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