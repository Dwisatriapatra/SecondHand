package com.example.secondhand.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.view.activity.InfoPenawarActivity
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.view.adapter.NotificationAdapter
import com.example.secondhand.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notifikasi.*

@AndroidEntryPoint
class NotifikasiFragment : Fragment() {

    private lateinit var userLoginTokenManager: UserLoginTokenManager
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifikasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        userLoginTokenManager = UserLoginTokenManager(requireContext())

        userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner){isUser ->
            if(isUser){
                notifikasi_belum_login_section.isInvisible = true

                val viewModelNotification = ViewModelProvider(this)[NotificationViewModel::class.java]
                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                    viewModelNotification.getAllNotification(it)
                }

                adapter = NotificationAdapter{item ->
                    userLoginTokenManager.name.asLiveData().observe(viewLifecycleOwner){
                        if(item.seller_name == it){
                            val intent = Intent(activity, InfoPenawarActivity::class.java)
                            intent.putExtra("InfoPenawaran", item)
                            startActivity(intent)
                        }else{
                            //take action
                        }
                    }
                }
                rv_notification.layoutManager = LinearLayoutManager(requireContext())
                rv_notification.adapter = adapter

                viewModelNotification.notification.observe(viewLifecycleOwner) {
                    adapter.setNotificationData(it)
                    notifikasi_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            }else{
                rv_notification.isInvisible = true
                notifikasi_progress_bar.isInvisible = true

                notifikasi_to_login_button.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }

    }


}