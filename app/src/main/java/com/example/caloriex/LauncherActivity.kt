package com.example.caloriex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startActivity(Intent(this, StartTrackingActivity::class.java))
            finish()
        } else {
            // If the user is not logged in, redirect them to the sign in screen:
            startActivity(Intent(this, GetStartedActivity::class.java))
            finish()
        }
    }
}