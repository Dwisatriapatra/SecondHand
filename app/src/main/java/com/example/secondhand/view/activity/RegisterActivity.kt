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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

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
                if(edtPassword.text!!.toString().length < 6){
                    Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                }else{
                    dataRegister()
                }
            }
        }
    }

    private fun dataRegister() {

        val fullName = edtNamaLengkap.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val email = edtEmail.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val password = edtPassword.text.toString().toRequestBody("multipart/form-data".toMediaType())

        //fake data
        val phone = 6282144.toString().toRequestBody("multipart/form-data".toMediaType())
        val address = "Default".toRequestBody("multipart/form-data".toMediaType())
        val city = "Default".toRequestBody("multipart/form-data".toMediaType())

        val viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        viewModel.userRegister(email,
            fullName,
            password,
            address,
            city,
            phone,
            //imageMultiPart!!
        )

        viewModel.responseMessage.observe(this){
            if(it!!){
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
        }

    }
}