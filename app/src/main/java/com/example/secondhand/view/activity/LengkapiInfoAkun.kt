package com.example.secondhand.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.reduceFileImage
import com.example.secondhand.helper.uriToFile
import com.example.secondhand.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_lengkapi_info_akun.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class LengkapiInfoAkun : AppCompatActivity() {

    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private var imageFile: File? = null
    private var imageUri: Uri? = null
    private var email: RequestBody? = null
    private var password: RequestBody? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lengkapi_info_akun)

        initView()
    }

    private fun initView() {
        update_image_user_button.setOnClickListener {
            startGallery()
        }

        update_button_simpan.setOnClickListener {
            val nama =
                update_nama_user.text.toString().toRequestBody("multipart/form-data".toMediaType())
            val alamat = update_alamat_user.text.toString()
                .toRequestBody("multipart/form-data".toMediaType())
            val kota =
                update_kota_user.text.toString().toRequestBody("multipart/form-data".toMediaType())
            val nomorHandphone = update_nomor_handphone_user.text.toString()
                .toRequestBody("multipart/form-data".toMediaType())

            if (
                update_nama_user.text.toString().isNotEmpty() &&
                update_alamat_user.text.toString().isNotEmpty() &&
                update_kota_user.text.toString().isNotEmpty() &&
                update_nomor_handphone_user.text.toString().isNotEmpty()
                && imageFile != null
            ) {
                val file = reduceFileImage(imageFile as File)
                val requestImageFile = file.asRequestBody("multipart/form-data".toMediaType())
                val imageMultiPart =
                    MultipartBody.Part.createFormData("image", file.name, requestImageFile)
                userLoginTokenManager = UserLoginTokenManager(this)
                userLoginTokenManager.email.asLiveData().observe(this) {
                    email = it.toRequestBody("multipart/form-data".toMediaType())
                }

                userLoginTokenManager.password.asLiveData().observe(this) {
                    password = it.toRequestBody("multipart/form-data".toMediaType())
                }

                userLoginTokenManager.accessToken.asLiveData().observe(this) {
                    val viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
                    viewModelUser.updateDataUser(
                        it,
                        alamat,
                        kota,
                        email!!,
                        nama,
                        imageMultiPart,
                        password!!,
                        nomorHandphone
                    )
                    viewModelUser.responseMessage.observe(this) { responseMessage ->
                        if (responseMessage!!) {

                            GlobalScope.launch {
                                userLoginTokenManager.saveUpdateAkun(
                                    imageUri.toString(),
                                    update_kota_user.text.toString(),
                                    update_alamat_user.text.toString(),
                                    update_nomor_handphone_user.text.toString()
                                )
                            }
                            Toast.makeText(this, "Update data profile berhasil", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(this, "Update gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val myImageFile = uriToFile(selectedImg, this)
            imageFile = myImageFile
            imageUri = selectedImg
            update_image_user_uri.setImageURI(selectedImg)
        }
    }
}