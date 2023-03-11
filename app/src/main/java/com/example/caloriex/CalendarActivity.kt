package com.example.caloriex

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.RequiresApi

class CalendarActivity : AppCompatActivity() {

    private var selectedDate: String = getCurrentDayString()
    private lateinit var calendarView: CalendarView
    private lateinit var okTextView: TextView
    private lateinit var cancelTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Remove the title bar from the popup activity
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendar_view)
        okTextView = findViewById(R.id.ok_textview)
        cancelTextView = findViewById(R.id.cancel_textview)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = constructDate(year, month + 1, dayOfMonth)
        }

        // No new date just go back to dashboard
        cancelTextView.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        // Sends the new date
        okTextView.setOnClickListener {
            intentToDashboard(selectedDate)
        }

        // Get the dialog window and set its size
        val dialogWindow: Window? = window
        dialogWindow?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun intentToDashboard(newDate: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("date", newDate)
        startActivity(intent)
    }

    override fun onBackPressed() {
        // Do nothing
    }
}