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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secondhand.R
import com.example.secondhand.datastore.UserLoginTokenManager
import com.example.secondhand.helper.NotificationItemClickListener
import com.example.secondhand.model.GetAllNotificationResponseItem
import com.example.secondhand.model.NotificationStatus
import com.example.secondhand.view.activity.InfoPenawarActivity
import com.example.secondhand.view.activity.LoginActivity
import com.example.secondhand.view.adapter.NotificationAdapter
import com.example.secondhand.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_notifikasi.*

@AndroidEntryPoint
class NotifikasiFragment : Fragment(), NotificationItemClickListener {

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

        userLoginTokenManager.isUser.asLiveData().observe(viewLifecycleOwner) { isUser ->
            if (isUser) {
                notifikasi_belum_login_section.isInvisible = true

                val viewModelNotification =
                    ViewModelProvider(this)[NotificationViewModel::class.java]
                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) {
                    viewModelNotification.getAllNotification(it)
                }
                adapter = NotificationAdapter(this@NotifikasiFragment)
                val myLm = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                myLm.stackFromEnd = true
                rv_notification.layoutManager = myLm
                rv_notification.adapter = adapter

                viewModelNotification.notification.observe(viewLifecycleOwner) {
                    adapter.setNotificationData(it)
                    notifikasi_progress_bar.isInvisible = true
                    adapter.notifyDataSetChanged()
                }
            } else {
                rv_notification.isInvisible = true
                notifikasi_progress_bar.isInvisible = true

                notifikasi_to_login_button.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }

    }

    override fun clickOnNotificationReadStatus(
        item: GetAllNotificationResponseItem,
        position: Int
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle("Status Notifikasi")
            .setMessage("Tandai notifikasi sudah dibaca?")
            .setNegativeButton("BATAL"){dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .setPositiveButton("YA"){_: DialogInterface, _: Int ->
                userLoginTokenManager = UserLoginTokenManager(requireContext())
                val viewModelNotification = ViewModelProvider(this)[NotificationViewModel::class.java]
                userLoginTokenManager.accessToken.asLiveData().observe(viewLifecycleOwner) { accessToken ->
                    viewModelNotification.updateNotificationStatus(
                        accessToken,
                        item.id,
                        NotificationStatus(true, item.status)
                    )
                    viewModelNotification.getAllNotification(accessToken)
                    viewModelNotification.notification.observe(viewLifecycleOwner){
                        adapter.setNotificationData(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            .show()
    }

    override fun clickOnNotificationBodySection(
        item: GetAllNotificationResponseItem,
        position: Int
    ) {
        userLoginTokenManager = UserLoginTokenManager(requireContext())
        userLoginTokenManager.name.asLiveData().observe(viewLifecycleOwner) { sellerName ->
            if (item.seller_name == sellerName) {
                val intent = Intent(activity, InfoPenawarActivity::class.java)
                intent.putExtra("InfoPenawaran", item)
                startActivity(intent)
            }
        }
    }


}