package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ProfileDetailsActivity : AppCompatActivity() {

    private lateinit var continueBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_details)

        // This will only occur the first time a user is creating an account in the app and requires them to setup their profiled details
        // Implement the logic here
        changeActivity()
    }

    private fun changeActivity() {
        continueBtn = findViewById(R.id.continue_button)

        // Waiting for the click event from user. Once done so, this will prompt EnergySettingsActivity
        continueBtn.setOnClickListener {
            val intent = Intent(this, EnergySettingsActivity::class.java)
            startActivity(intent)
        }
    }
}