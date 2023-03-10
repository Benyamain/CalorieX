package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MessageMotivationActivity : AppCompatActivity() {

    private lateinit var definitelyBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_motivation)

        changeActivity()
    }

    private fun changeActivity() {
        definitelyBtn = findViewById(R.id.definitely_button)

        // Waiting for the click event from user. Once done so, this will prompt DashboardActivity
        definitelyBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}