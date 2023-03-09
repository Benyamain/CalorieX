package com.example.caloriex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ExistingLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_login)

        /* Going to be set to true once the user logins in successfully

        // When the user successfully signs in:
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()

        */
    }
}