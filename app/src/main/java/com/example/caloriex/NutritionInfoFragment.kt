package com.example.caloriex

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    activity?.runOnUiThread {
                        view?.findViewById<ProgressBar>(R.id.ni_progress_circular)?.visibility =
                            View.VISIBLE
                    }
                    delay(450)
                    activity?.runOnUiThread {
                        navController.navigate(R.id.dashboardFragment)
                    }
                }
            }
        }

        userEmail?.let { encodeEmail(it) }?.let { emailEncoded ->
            var key = ""
            Firebase.database.reference.child("foodSelectionKeys").child(emailEncoded)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            key = dataSnapshot.getValue(FoodItemKey::class.java)?.key ?: ""
                        }

                        userEmail?.let { encodeEmail(it) }?.let { emailEncoded ->
                            Firebase.database.reference.child("foodSelection").child(emailEncoded)
                                .child(key)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val foodItem =
                                                dataSnapshot.getValue(FoodItem::class.java)

                                            // Now you can access the values in the `foodItem` object
                                            lifecycleScope.launch {
                                                withContext(Dispatchers.IO) {
                                                    activity?.runOnUiThread {
                                                        val weightUnits = " g"
                                                        val calorieUnits = " kcal"
                                                        nutritionInfoTv.text = foodItem?.name ?: ""
                                                        detailedProteinsValue.text =
                                                            foodItem?.protein?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedKcalValue.text =
                                                            foodItem?.calorie?.let { if (it == "null") "0" else it } + calorieUnits
                                                        detailedCarbsValue.text =
                                                            foodItem?.carbs?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedFatsValue.text =
                                                            foodItem?.fat?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionCaloriesValue.text =
                                                            foodItem?.calorie?.let { if (it == "null") "0" else it } + calorieUnits
                                                        detailedNutritionProteinValue.text =
                                                            foodItem?.protein?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionFatsValue.text =
                                                            foodItem?.fat?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionCarbsValue.text =
                                                            foodItem?.carbs?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionSValue.text =
                                                            foodItem?.satfat?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionPolyValue.text =
                                                            foodItem?.polyfat?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionMonoValue.text =
                                                            foodItem?.monofat?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionSugarValue.text =
                                                            foodItem?.sugar?.let { if (it == "null") "0" else it } + weightUnits
                                                        detailedNutritionFiberValue.text =
                                                            foodItem?.fiber?.let { if (it == "null") "0" else it } + weightUnits
                                                        amountEt.setText("")
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Handle database error
                                    }
                                })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }
}
