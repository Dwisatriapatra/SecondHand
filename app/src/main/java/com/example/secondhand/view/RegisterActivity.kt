package com.example.secondhand.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.secondhand.R
import com.example.secondhand.datastore.UserRegisterManager
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    lateinit var userRegisterManager : UserRegisterManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide() // hide the title bar
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) //enable full screen
        setContentView(R.layout.activity_register)

        userRegisterManager = UserRegisterManager(this)

        btnDaftar.setOnClickListener {
            if (edtNamaLengkap.text.isNotEmpty() &&
                    edtEmail.text.isNotEmpty() &&
                    edtPassword.text.isNotEmpty()){

                dataRegister()
            }else{
                Toast.makeText(this, "Data kosong", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun dataRegister(){
        val email = edtEmail.text.toString()
        val namaLengkap = edtNamaLengkap.text.toString()
        val password = edtPassword.text.toString()

        val viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.userReg.observe(this) {
            GlobalScope.launch {
                userRegisterManager.saveData(email, namaLengkap, password)
                Toast.makeText(this@RegisterActivity, "Data berhasil disimpan", Toast.LENGTH_LONG)
                    .show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
        viewModel.userRegister(email,namaLengkap, password)
    }
}