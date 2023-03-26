package com.example.caloriex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

class NutritionInfoFragment : Fragment() {

    private lateinit var saveBtn: Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var nutritionInfoTv: TextView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition_info, container, false)
        toolbar = view.findViewById(R.id.appear_bottom_toolbar)
        navController = findNavController()
        imageIv = view.findViewById(R.id.appear_bottom_toolbar_close_image_view)
        nutritionInfoTv = view.findViewById(R.id.appear_bottom_toolbar_title_textview)
        nutritionInfoTv.text = "Food Name"

        imageIv.setOnClickListener {
            navController.popBackStack()
        }

        saveBtn = view.findViewById(R.id.nutrition_info_save_button)
        saveBtn.setOnClickListener {
            intentToDashboard()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    private fun intentToDashboard() {
       // val bundle = Bundle()
       // bundle.putString("date", newDate)

        navController.navigate(R.id.dashboardFragment)

    }
}
