package com.example.caloriex

import android.annotation.SuppressLint
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

class NutritionInfoFragment : Fragment() {

    private lateinit var saveBtn: Button
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var nutritionInfoTv: TextView
    private lateinit var navController: NavController
    private lateinit var detailedProteinsValue: TextView
    private lateinit var detailedKcalValue: TextView
    private lateinit var detailedCarbsValue: TextView
    private lateinit var detailedFatsValue: TextView
    private lateinit var detailedNutritionCaloriesValue: TextView
    private lateinit var detailedNutritionProteinValue: TextView
    private lateinit var detailedNutritionFatsValue: TextView
    private lateinit var detailedNutritionCarbsValue: TextView
    private lateinit var detailedNutritionSValue: TextView
    private lateinit var detailedNutritionPolyValue: TextView
    private lateinit var detailedNutritionMonoValue: TextView
    private lateinit var detailedNutritionSugarValue: TextView
    private lateinit var detailedNutritionFiberValue: TextView
    private lateinit var amountEt: EditText

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nutrition_info, container, false)
        toolbar = view.findViewById(R.id.appear_bottom_toolbar)
        navController = findNavController()
        imageIv = view.findViewById(R.id.appear_bottom_toolbar_close_image_view)
        nutritionInfoTv = view.findViewById(R.id.appear_bottom_toolbar_title_textview)
        detailedProteinsValue = view.findViewById(R.id.detailed_proteins_value)
        detailedKcalValue = view.findViewById(R.id.detailed_kcal_value)
        detailedCarbsValue = view.findViewById(R.id.detailed_carbs_value)
        detailedFatsValue = view.findViewById(R.id.detailed_fats_value)
        detailedNutritionCaloriesValue = view.findViewById(R.id.detailed_nutrition_calories_value)
        detailedNutritionProteinValue = view.findViewById(R.id.detailed_nutrition_proteins_value)
        detailedNutritionFatsValue = view.findViewById(R.id.detailed_nutrition_fats_value)
        detailedNutritionCarbsValue = view.findViewById(R.id.detailed_nutrition_carbs_value)
        detailedNutritionSValue = view.findViewById(R.id.detailed_nutrition_s_value)
        detailedNutritionPolyValue = view.findViewById(R.id.detailed_nutrition_poly_value)
        detailedNutritionMonoValue = view.findViewById(R.id.detailed_nutrition_mono_value)
        detailedNutritionSugarValue = view.findViewById(R.id.detailed_nutrition_sugars_value)
        detailedNutritionFiberValue = view.findViewById(R.id.detailed_nutrition_fiber_value)
        amountEt = view.findViewById(R.id.amount_edittext)

        imageIv.setOnClickListener {
            navController.popBackStack()
        }

        saveBtn = view.findViewById(R.id.nutrition_info_save_button)
        saveBtn.setOnClickListener {
            intentToDashboard()
        }

        // Read the date when the user navigates here
        // Condition below basically is a validation for all the attributes of that food such as its food content nutrition
        val bundle = arguments
        if ((bundle != null) && (bundle.containsKey("name"))) {
            val weightUnits = " g"
            val calorieUnits = " kcal"
            nutritionInfoTv.text = arguments?.getString("name").toString()
            detailedProteinsValue.text = arguments?.getString("protein")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedKcalValue.text = arguments?.getString("calorie")?.toString()
                ?.let { if (it == "null") "0" else it } + calorieUnits
            detailedCarbsValue.text = arguments?.getString("carbs")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedFatsValue.text = arguments?.getString("fat")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionCaloriesValue.text = arguments?.getString("calorie")?.toString()
                ?.let { if (it == "null") "0" else it } + calorieUnits
            detailedNutritionProteinValue.text = arguments?.getString("protein")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionFatsValue.text = arguments?.getString("fat")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionCarbsValue.text = arguments?.getString("carbs")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionSValue.text = arguments?.getString("satfat")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionPolyValue.text = arguments?.getString("polyfat")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionMonoValue.text = arguments?.getString("monofat")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionSugarValue.text = arguments?.getString("sugar")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            detailedNutritionFiberValue.text = arguments?.getString("fiber")?.toString()
                ?.let { if (it == "null") "0" else it } + weightUnits
            amountEt.setText("")
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

        view?.findViewById<ProgressBar>(R.id.ni_progress_circular)?.visibility = View.VISIBLE
        Handler().postDelayed({
            navController.navigate(R.id.dashboardFragment)
        }, 450)

    }
}
