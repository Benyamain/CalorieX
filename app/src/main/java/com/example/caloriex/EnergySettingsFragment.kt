package com.example.caloriex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class EnergySettingsFragment : Fragment() {

    private lateinit var continueBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_energy_settings, container, false)
        changeActivity(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    private fun changeActivity(view: View) {
        continueBtn = view.findViewById(R.id.second_continue_button)

        // Waiting for the click event from user. Once done so, this will prompt MacroRatiosFragment
        continueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_energySettingsFragment_to_macroRatiosFragment)
        }

        val activityLevelAutocompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.activity_level_autocomplete_textview)
        val activityLevelOptions = resources.getStringArray(R.array.activity_level_options)

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, activityLevelOptions)
        activityLevelAutocompleteTextView.setAdapter(adapter)

        activityLevelAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            activityLevelAutocompleteTextView.setSelection(position)
        }

        val bmrAutocompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.bmr_autocomplete_textview)
        val bmrOptions = resources.getStringArray(R.array.bmr_options)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, bmrOptions)
        bmrAutocompleteTextView.setAdapter(adapter)

        bmrAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            bmrAutocompleteTextView.setSelection(position)
        }
    }
}