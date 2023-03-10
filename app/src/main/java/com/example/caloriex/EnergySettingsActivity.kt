package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EnergySettingsActivity : AppCompatActivity() {

    private lateinit var continueBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_energy_settings)

        changeActivity()
    }

    private fun changeActivity() {
        continueBtn = findViewById(R.id.second_continue_button)

        // Waiting for the click event from user. Once done so, this will prompt MessageMotivationActivity
        continueBtn.setOnClickListener {
            val intent = Intent(this, MessageMotivationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}