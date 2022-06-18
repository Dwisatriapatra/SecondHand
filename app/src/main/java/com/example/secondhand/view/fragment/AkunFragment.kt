package com.example.secondhand.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.view.activity.SplashAcivity
import kotlinx.android.synthetic.main.fragment_akun.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        //tested
        akun_logout_section.setOnClickListener{
            AlertDialog.Builder(requireContext())
                .setTitle("LOG OUT")
                .setMessage("Anda yakin ingin logout?")
                .setNegativeButton("Tidak"){dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }
                .setPositiveButton("Ya"){_: DialogInterface, _:Int ->
                    GlobalScope.launch {
                        userLoginTokenManager.clearToken()
                        // code for refreshing apps
                        // tested
                        val mIntent = Intent(requireContext(), SplashAcivity::class.java)
                        startActivity(mIntent)

                    }
                }
                .show()
        }

    }
}