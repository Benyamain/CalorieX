package com.example.caloriex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AccountSignInActivity : AppCompatActivity() {

    private lateinit var alreadyHaveAccountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_sign_in)

        alreadyHaveAccountTextView = findViewById(R.id.already_have_account_textview)

        // Send user to do native login if they already have an account
        alreadyHaveAccountTextView.setOnClickListener {
            val intent = Intent(this, ExistingLoginActivity::class.java)
            startActivity(intent)
        }

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