package com.example.secondhand.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.view.activity.HistoryActivity
import com.example.secondhand.view.activity.LengkapiInfoAkun
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.view.activity.SplashAcivity
import com.example.secondhand.viewmodel.SellerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_akun.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AkunFragment : Fragment() {

    private lateinit var userLoginTokenManager: UserLoginTokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLoginTokenManager = UserLoginTokenManager(requireContext())

        userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner) { isUser ->
            if (isUser) {
                akun_belum_login_section.isInvisible = true
                initView()
            } else {
                text_akun_saya.isInvisible = true
                akun_image.isInvisible = true
                akun_edit_profile_section.isInvisible = true
                akun_history_section.isInvisible = true
                akun_pengaturan_section.isInvisible = true
                akun_logout_section.isInvisible = true
                akun_to_login.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }
    }

    private fun initView() {

        userLoginTokenManager = UserLoginTokenManager(requireContext())
        val viewModelUser = ViewModelProvider(this)[SellerViewModel::class.java]

        userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
            viewModelUser.getSellerData(it)
            viewModelUser.seller.observe(viewLifecycleOwner) { sellerData ->
                Glide.with(akun_image.context)
                    .load(sellerData.image_url)
                    .error(R.drawable.ic_launcher_background)
                    .into(akun_image)
            }
        }

        akun_logout_section.setOnClickListener {
            userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner) { isUser ->
                if (isUser) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("LOG OUT")
                        .setMessage("Anda yakin ingin logout?")
                        .setNegativeButton("Tidak") { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                        }
                        .setPositiveButton("Ya") { _: DialogInterface, _: Int ->
                            GlobalScope.launch {
                                userLoginTokenManager.clearToken()
                                // code for refreshing apps
                                val mIntent = Intent(requireContext(), SplashAcivity::class.java)
                                startActivity(mIntent)

                            }
                        }
                        .show()
                } else {
                    //
                }
            }

        }

        akun_edit_profile_section.setOnClickListener {
            startActivity(Intent(activity, LengkapiInfoAkun::class.java))
        }

        akun_history_section.setOnClickListener {
            startActivity(Intent(activity, HistoryActivity::class.java))
        }

        akun_pengaturan_section.setOnClickListener {
            throw RuntimeException("Crashlytics demo")
        }
    }
}