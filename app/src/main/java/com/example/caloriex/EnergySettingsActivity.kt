package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class EnergySettingsActivity : AppCompatActivity() {

    private lateinit var dashboardBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_energy_settings)

        changeActivity()
    }

    private fun changeActivity() {
        dashboardBtn = findViewById(R.id.dashboard_button)

        // Waiting for the click event from user. Once done so, this will prompt DashboardActivity
        dashboardBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}