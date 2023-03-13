package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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

class ProfileDetailsFragment : Fragment() {

    private lateinit var continueBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActivity()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    private fun changeActivity() {
        continueBtn = view?.findViewById(R.id.continue_button)!!

        // Waiting for the click event from user. Once done so, this will prompt EnergySettingsActivity
        continueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileDetailsFragment_to_energySettingsFragment)
        }

        val sexOptionsAutocompleteTextView =
            view?.findViewById<AutoCompleteTextView>(R.id.sex_spinner_box_dropdown)!!
        val sexOptions = resources.getStringArray(R.array.sex_options)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sexOptions)
        sexOptionsAutocompleteTextView.setAdapter(adapter)

        sexOptionsAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            sexOptionsAutocompleteTextView.setSelection(position)
        }
    }
}