package com.example.secondhand.view.activity

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.secondhand.R
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private var imageMultiPart: MultipartBody.Part? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtMasukDisini.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        register_choose_image_button.setOnClickListener {
            startGallery()
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
        val address = "init address".toRequestBody("multipart/form-data".toMediaType())
        val city = "init city".toRequestBody("multipart/form-data".toMediaType())

        val viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.userRegister(email, fullName, password, address, city, phone, imageMultiPart!!)

        viewModel.responseMessage.observe(this){
            if(it!!){
                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startGallery() {
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = this.contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                register_image.setImageURI(it)

                val tempFile = File.createTempFile("temp-", null, null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart =
                    MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }
}