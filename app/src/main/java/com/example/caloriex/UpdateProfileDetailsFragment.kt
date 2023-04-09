package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class UpdateProfileDetailsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var settingsTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_profile_details, container, false)
        toolbar = view.findViewById(R.id.settings_toolbar)
        navController = findNavController()
        imageIv = view.findViewById(R.id.left_arrow_image_view)
        settingsTv = view.findViewById(R.id.settings_title_textview)
        settingsTv.text = "Profile Details"

        imageIv.setOnClickListener {
            navController.navigate(R.id.action_updateProfileDetailsFragment_to_settingsFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActivity()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    private fun changeActivity() {
        val sexOptionsAutocompleteTextView =
            view?.findViewById<AutoCompleteTextView>(R.id.profile_details_sex_spinner_box_dropdown)!!
        val sexOptions = resources.getStringArray(R.array.sex_options)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sexOptions)
        sexOptionsAutocompleteTextView.setAdapter(adapter)

        sexOptionsAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            sexOptionsAutocompleteTextView.setSelection(position)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}