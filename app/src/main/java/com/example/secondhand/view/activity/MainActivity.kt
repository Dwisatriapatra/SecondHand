package com.example.secondhand.view.activity

import android.R.attr.delay
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.secondhand.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var myLooper: Handler? = null
    private var myRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navHostFragment = fragmentContainerView as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView = main_bottom_navigation
        setupWithNavController(bottomNavigationView, navController)
    }

    //turn of back button when in main activity
    override fun onBackPressed() {
        //super.onBackPressed()
    }
}