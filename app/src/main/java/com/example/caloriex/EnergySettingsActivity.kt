package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

        val activityLevelAutocompleteTextView =
            findViewById<AutoCompleteTextView>(R.id.activity_level_autocomplete_textview)
        val activityLevelOptions = resources.getStringArray(R.array.activity_level_options)

        var adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, activityLevelOptions)
        activityLevelAutocompleteTextView.setAdapter(adapter)

        activityLevelAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            activityLevelAutocompleteTextView.setSelection(position)
        }

        val bmrAutocompleteTextView =
            findViewById<AutoCompleteTextView>(R.id.bmr_autocomplete_textview)
        val bmrOptions = resources.getStringArray(R.array.bmr_options)

        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bmrOptions)
        bmrAutocompleteTextView.setAdapter(adapter)

        bmrAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            bmrAutocompleteTextView.setSelection(position)
        }
    }

    override fun onBackPressed() {
        // Do nothing
    }
}