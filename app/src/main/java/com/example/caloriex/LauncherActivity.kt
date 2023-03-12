package com.example.caloriex

import DashboardFragment
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class LauncherActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            navController.graph.setStartDestination(R.id.startTrackingFragment)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            navController.graph.setStartDestination(R.id.getStartedFragment)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}