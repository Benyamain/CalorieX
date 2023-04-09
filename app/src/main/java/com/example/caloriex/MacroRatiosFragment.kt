package com.example.caloriex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MacroRatiosFragment : Fragment() {

    private lateinit var continueBtn: Button
    private lateinit var proteinEt: EditText
    private lateinit var netCarbsEt: EditText
    private lateinit var fatsEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_macro_ratios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActivity()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        proteinEt = view.findViewById(R.id.protein_edittext)
        netCarbsEt = view.findViewById(R.id.net_carbs_edittext)
        fatsEt = view.findViewById(R.id.fat_edittext)
    }

    private fun changeActivity() {
        continueBtn = view?.findViewById(R.id.macro_ratios_continue_button)!!

        // Waiting for the click event from user. Once done so, this will prompt MessageMotivationFragment
        continueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_macroRatiosFragment_to_messageMotivationFragment)
        }
    }
}