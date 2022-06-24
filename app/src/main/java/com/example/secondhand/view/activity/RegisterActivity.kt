package com.example.secondhand.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.secondhand.R
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtMasukDisini.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnDaftar.setOnClickListener {
            if (edtNamaLengkap.text!!.isNotEmpty() &&
                edtEmail.text!!.isNotEmpty() &&
                edtPassword.text!!.isNotEmpty()
            ) {
                dataRegister()
                Toast.makeText(this, "Data tersimpan", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Data kosong", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun dataRegister() {
        val fullName = edtNamaLengkap.text.toString()
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()

        val viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.userRegister(fullName, email, password)
    }
}