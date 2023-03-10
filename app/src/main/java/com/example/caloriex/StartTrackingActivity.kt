package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartTrackingActivity : AppCompatActivity() {

    private lateinit var startTrackingBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_tracking)

        goDashboard()
    }

    private fun goDashboard() {
        startTrackingBtn = findViewById(R.id.start_tracking_button)

        // Set click listener on the button to launch the dashboard:
        startTrackingBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}