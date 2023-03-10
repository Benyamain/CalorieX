package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button

class LogoutActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        changeActivity()
    }

    private fun changeActivity() {
        logoutBtn = findViewById(R.id.logout_button)

        // Waiting for the click event from user. Once done so, this will prompt AccountSignInActivity
        logoutBtn.setOnClickListener {
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
        }
    }
}