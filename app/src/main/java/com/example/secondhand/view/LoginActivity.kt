package com.example.secondhand.view

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var userLoginTokenManager: UserLoginTokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //enable full screen
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
            viewModelUser.userLogin(LoginRequestUser(email, password))
            viewModelUser.responseMessage.observe(this){responseMessage ->
                if(responseMessage){
                    viewModelUser.user.observe(this){
                        saveToken(it)
                    }
                }else{
                    Toast.makeText(this, "Email/password salah", Toast.LENGTH_SHORT).show()
                }
            }

        }else{

            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToken(loginResponsePostUser: LoginResponsePostUser) {
        userLoginTokenManager = UserLoginTokenManager(this)
        lifecycleScope.launch {
            withContext(Dispatchers.Main){
                userLoginTokenManager.setBoolean(true)
                userLoginTokenManager.saveToken(
                    loginResponsePostUser.email,
                    loginResponsePostUser.name,
                    loginResponsePostUser.access_token
                )
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }
    }
}