package com.example.caloriex

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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class UpdateMacroRatiosFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var settingsTv: TextView
    private lateinit var proteinEt: EditText
    private lateinit var netCarbsEt: EditText
    private lateinit var fatsEt: EditText
    private lateinit var progressBar: ProgressBar

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
        progressBar = view.findViewById(R.id.um_progress_circular)
        settingsTv.text = "Macronutrient Ratios"

        imageIv.setOnClickListener {
            lifecycleScope.launch {
                if (proteinEt.text.toString().isNotEmpty() && netCarbsEt.text.toString().isNotEmpty() && fatsEt.text.toString().isNotEmpty()) {
                    val total =
                        proteinEt.text.toString().toDouble().plus(netCarbsEt.text.toString().toDouble())
                            .plus(fatsEt.text.toString().toDouble())
                    if (total == 100.0) {
                        progressBar.visibility =
                            View.VISIBLE
                        Handler().postDelayed({
                            navController.navigate(R.id.action_updateMacroRatiosFragment_to_settingsFragment)
                        }, 1000)

                        userEmail?.let { encodeEmail(it) }?.let {
                            Firebase.database.getReference("energyExpenditure")
                                .child(it)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val energyExp = if (dataSnapshot.value is Long) {
                                                CalorieAmount(
                                                    dataSnapshot.getValue(Long::class.java)?.toInt()
                                                )
                                            } else {
                                                dataSnapshot.getValue(CalorieAmount::class.java)
                                            }
                                            val calorie = energyExp?.calories ?: 0
                                            val ratioCalories = calculateMacronutrientRatios(
                                                calorie,
                                                proteinEt.text.toString().toDouble(),
                                                netCarbsEt.text.toString().toDouble(),
                                                fatsEt.text.toString().toDouble()
                                            )
                                            Firebase.database.reference.child("macroRatioCalories")
                                                .child(encodeEmail(userEmail)).setValue(ratioCalories)
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.d("databaseError", "$databaseError")
                                    }
                                })
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Ratios must add up to 100!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fill out the required fields!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.d("macroRatios", "proteinEt is: ${proteinEt.text}")
                Log.d("macroRatios", "netCarbsEt is: ${netCarbsEt.text}")
                Log.d("macroRatios", "fatsEt is: ${fatsEt.text}")
            }

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

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE

            userEmail?.let { encodeEmail(it) }?.let {
                Firebase.database.getReference("macroRatios")
                    .child(it)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val macros = dataSnapshot.getValue(MacroRatios::class.java)
                                proteinEt.setText(macros?.proteinRatio.toString())
                                netCarbsEt.setText(macros?.netCarbRatio.toString())
                                fatsEt.setText(macros?.fatRatio.toString())
                            }
                            progressBar.visibility = View.GONE
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d("databaseError", "$databaseError")
                            progressBar.visibility = View.GONE
                        }
                    })
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}