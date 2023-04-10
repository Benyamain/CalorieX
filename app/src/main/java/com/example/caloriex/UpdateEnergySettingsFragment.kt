package com.example.caloriex

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class UpdateEnergySettingsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var settingsTv: TextView
    private lateinit var weightGoalEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_energy_settings, container, false)
        toolbar = view.findViewById(R.id.settings_toolbar)
        navController = findNavController()
        imageIv = view.findViewById(R.id.left_arrow_image_view)
        settingsTv = view.findViewById(R.id.settings_title_textview)
        settingsTv.text = "Energy Settings"
        weightGoalEt = view.findViewById(R.id.energy_settings_weight_goal_edittext)

        imageIv.setOnClickListener {
            view.findViewById<ProgressBar>(R.id.ue_progress_circular).visibility = View.VISIBLE
            Handler().postDelayed({
                navController.navigate(R.id.action_updateEnergySettingsFragment_to_settingsFragment)
            }, 1000)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActivity(view)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    private fun changeActivity(view: View) {
        val activityLevelAutocompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.energy_settings_activity_level_autocomplete_textview)
        val activityLevelOptions = resources.getStringArray(R.array.activity_level_options)

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, activityLevelOptions)
        activityLevelAutocompleteTextView.setAdapter(adapter)

        activityLevelAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            activityLevelAutocompleteTextView.setSelection(position)
        }

        val bmrAutocompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.energy_settings_bmr_autocomplete_textview)
        val bmrOptions = resources.getStringArray(R.array.bmr_options)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, bmrOptions)
        bmrAutocompleteTextView.setAdapter(adapter)

        bmrAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            bmrAutocompleteTextView.setSelection(position)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}