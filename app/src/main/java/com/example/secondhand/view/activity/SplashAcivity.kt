package com.example.secondhand.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager

class SplashAcivity : AppCompatActivity() {

    private lateinit var userLoginTokenManager: UserLoginTokenManager

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
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        }, 3000)

    }
}