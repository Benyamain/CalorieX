package com.example.caloriex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GetStartedActivity : AppCompatActivity() {

    private lateinit var getStartedBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        changeActivity()
    }

    private fun changeActivity() {
        getStartedBtn = findViewById(R.id.get_started_button)

        // Waiting for the click event from user. Once done so, this will prompt AccountSignInActivity
        getStartedBtn.setOnClickListener {
            val intent = Intent(this, AccountSignInActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}