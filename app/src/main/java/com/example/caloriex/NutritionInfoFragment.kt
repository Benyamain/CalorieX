package com.example.caloriex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class NutritionInfoFragment : Fragment() {

    private lateinit var cancelBtn: Button
    private lateinit var saveBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nutrition_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        cancelBtn = view.findViewById(R.id.cancel_button)
        cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        saveBtn = view.findViewById(R.id.save_button)
        saveBtn.setOnClickListener {
            intentToDashboard()
        }
    }

    private fun intentToDashboard() {
        //val bundle = Bundle()
       // bundle.putString("date", newDate)

        val destinationFragment = DashboardFragment()
      //  destinationFragment.arguments = bundle

        //findNavController().navigate(R.id.dashboard)
    }
}
