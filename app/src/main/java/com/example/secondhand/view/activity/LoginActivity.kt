package com.example.secondhand.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var viewModelUser: UserViewModel
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        userLoginTokenManager = UserLoginTokenManager(this)
        biometricPrompt = createBiometricPrompt()
        BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)

        button_biometric.setOnClickListener {

            // Menampilkan Biometric Authentication
            biometricPrompt.authenticate(createPromptInfo())
        }


        button_login.setOnClickListener {
            login()
        }

        login_to_register_akun.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }


    private fun login() {
        viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]
        val email = login_input_email.text.toString()
        val password = login_input_password.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            if(password.length < 6){
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }else{
                viewModelUser.userLogin(LoginRequestUser(email, password))
                viewModelUser.responseMessage.observe(this) { responseMessage ->
                    if (responseMessage!!) {
                        viewModelUser.user.observe(this) {
                            //need to get updated password that input by user
                            //better update email if val email is needed here
                            val getUpdatedPassword = login_input_password.text.toString()
                            saveToken(it, getUpdatedPassword)
                        }
                    } else {
                        Toast.makeText(this, "Email/password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToken(loginResponsePostUser: LoginResponsePostUser, password: String) {
        userLoginTokenManager = UserLoginTokenManager(this)
        Log.d("ceking3", "${loginResponsePostUser.email}, $password")
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                userLoginTokenManager.clearToken()
                userLoginTokenManager.setBoolean(true)
                userLoginTokenManager.setIsUser(true)
                userLoginTokenManager.saveToken(
                    loginResponsePostUser.email,
                    loginResponsePostUser.name,
                    loginResponsePostUser.access_token,
                    password
                )
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setDescription("Description")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setConfirmationRequired(true)
            .build()
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        // Callback
        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {

                // Authentikasi Error
                Toast.makeText(this@LoginActivity, errString.toString(), Toast.LENGTH_SHORT).show();

                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {

                // Authentikasi Berhasil
                Toast.makeText(this@LoginActivity, "Authentication Success", Toast.LENGTH_SHORT).show();

                super.onAuthenticationSucceeded(result)
            }

            override fun onAuthenticationFailed() {

                // Authentikasi Gagal
                Toast.makeText(this@LoginActivity, "Authentication Failed", Toast.LENGTH_SHORT).show();

                super.onAuthenticationFailed()
            }
        }

        return BiometricPrompt(this, executor, callback)
    }


}