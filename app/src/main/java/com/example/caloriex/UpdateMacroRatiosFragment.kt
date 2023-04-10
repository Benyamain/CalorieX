package com.example.caloriex

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

class UpdateMacroRatiosFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var settingsTv: TextView
    private lateinit var proteinEt: EditText
    private lateinit var netCarbsEt: EditText
    private lateinit var fatsEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_macro_ratios, container, false)
        toolbar = view.findViewById(R.id.settings_toolbar)
        navController = findNavController()
        imageIv = view.findViewById(R.id.left_arrow_image_view)
        settingsTv = view.findViewById(R.id.settings_title_textview)
        settingsTv.text = "Macronutrient Ratios"

        imageIv.setOnClickListener {
            view.findViewById<ProgressBar>(R.id.um_progress_circular).visibility = View.VISIBLE
            Handler().postDelayed({
                navController.navigate(R.id.action_updateMacroRatiosFragment_to_settingsFragment)
            }, 1000)
        }

        proteinEt = view.findViewById(R.id.macro_ratios_protein_edittext)
        netCarbsEt = view.findViewById(R.id.macro_ratios_net_carbs_edittext)
        fatsEt = view.findViewById(R.id.macro_ratios_fat_edittext)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    override fun onResume() {
        super.onResume()
    }
}