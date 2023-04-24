package com.example.caloriex

import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateEnergySettingsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var settingsTv: TextView
    private lateinit var weightGoalEt: EditText
    private lateinit var activityLevelAutocompleteTextView: AutoCompleteTextView
    private lateinit var bmrAutocompleteTextView: AutoCompleteTextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update_energy_settings, container, false)
        toolbar = view.findViewById(R.id.settings_toolbar)
        progressBar = view.findViewById(R.id.ue_progress_circular)
        navController = findNavController()
        imageIv = view.findViewById(R.id.left_arrow_image_view)
        settingsTv = view.findViewById(R.id.settings_title_textview)
        settingsTv.text = "Energy Settings"
        weightGoalEt = view.findViewById(R.id.energy_settings_weight_goal_edittext)

        lifecycleScope.launch {
            changeActivity(view)
        }

        imageIv.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (bmrAutocompleteTextView.text.toString()
                            .isNotEmpty() && activityLevelAutocompleteTextView.text.toString()
                            .isNotEmpty() && weightGoalEt.text.toString().isNotEmpty()
                    ) {
                        activity?.runOnUiThread {
                            progressBar.visibility = View.VISIBLE
                        }
                        delay(1000)
                        activity?.runOnUiThread {
                            navController.navigate(R.id.action_updateEnergySettingsFragment_to_settingsFragment)
                        }

                        energySettings(
                            bmrAutocompleteTextView.text.toString(),
                            activityLevelAutocompleteTextView.text.toString(),
                            weightGoalEt.text.toString().toDouble()
                        )

                        Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/profileDetails")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val profileDetails =
                                            dataSnapshot.getValue(ProfileDetails::class.java)
                                        val age = profileDetails?.age ?: 0
                                        val height = profileDetails?.height ?: 0.0
                                        val weight = profileDetails?.weight?.get(profileDetails?.weight?.lastIndex ?: 0)?.toDouble() ?: 0.0
                                        val sex = profileDetails?.sex ?: ""

                                        if (dataSnapshot.exists()) {
                                            if (bmrAutocompleteTextView.text.toString() == "Harris Benedict") {
                                                val hb = calculateBMRHarrisBenedict(
                                                    sex,
                                                    weight,
                                                    height,
                                                    age,
                                                    activityLevelAutocompleteTextView.text.toString(),
                                                    weightGoalEt.text.toString().toDouble()
                                                )
                                                Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/energy/energyExpenditure").setValue(hb)
                                            } else {
                                                val msj = calculateBMRMifflinStJeor(
                                                    sex,
                                                    weight,
                                                    height,
                                                    age,
                                                    activityLevelAutocompleteTextView.text.toString(),
                                                    weightGoalEt.text.toString().toDouble()
                                                )
                                                Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/energy/energyExpenditure").setValue(msj)
                                            }

                                            Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/macros/macroRatios")
                                                    .addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                                val macroRatios =
                                                                    dataSnapshot.getValue(
                                                                        MacroRatios::class.java
                                                                    )

                                                                Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/energy/energyExpenditure")
                                                                            .addListenerForSingleValueEvent(
                                                                                object :
                                                                                    ValueEventListener {
                                                                                    override fun onDataChange(
                                                                                        dataSnapshot: DataSnapshot
                                                                                    ) {
                                                                                        if (dataSnapshot.exists()) {
                                                                                            val energyExp =
                                                                                                if (dataSnapshot.value is Long) {
                                                                                                    CalorieAmount(
                                                                                                        dataSnapshot.getValue(
                                                                                                            Long::class.java
                                                                                                        )
                                                                                                            ?.toInt()
                                                                                                    )
                                                                                                } else {
                                                                                                    dataSnapshot.getValue(
                                                                                                        CalorieAmount::class.java
                                                                                                    )
                                                                                                }
                                                                                            val calorie =
                                                                                                energyExp?.calories
                                                                                                    ?: 0
                                                                                            val ratioCalories =
                                                                                                calculateMacronutrientRatios(
                                                                                                    calorie,
                                                                                                    macroRatios?.proteinRatio
                                                                                                        ?: 0.0,
                                                                                                    macroRatios?.netCarbRatio
                                                                                                        ?: 0.0,
                                                                                                    macroRatios?.fatRatio
                                                                                                        ?: 0.0
                                                                                                )

                                                                                            Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/macros/macroRatioCalories").setValue(ratioCalories)
                                                                                        }
                                                                                    }

                                                                                    override fun onCancelled(
                                                                                        databaseError: DatabaseError
                                                                                    ) {
                                                                                        Log.d(
                                                                                            "databaseError",
                                                                                            "$databaseError"
                                                                                        )
                                                                                    }
                                                                                })
                                                            }
                                                        }

                                                        override fun onCancelled(databaseError: DatabaseError) {
                                                            Log.d("databaseError", "$databaseError")
                                                        }
                                                    })
                                        } else {
                                            Log.d("dataSnapshot", "No data found")
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.d("databaseError", "$databaseError")
                                    }
                                })
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Fill out all the required fields!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE

            Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/energy/energySettings")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val energySettings = dataSnapshot.getValue(EnergySettings::class.java)
                                activityLevelAutocompleteTextView.setText(energySettings?.activityLevel)
                                bmrAutocompleteTextView.setText(energySettings?.bmrName)
                                weightGoalEt.setText(energySettings?.weightGoal.toString())
                                changeActivity(view)
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

    private fun changeActivity(view: View) {
        activityLevelAutocompleteTextView =
            view.findViewById(R.id.energy_settings_activity_level_autocomplete_textview)
        val activityLevelOptions = resources.getStringArray(R.array.activity_level_options)

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, activityLevelOptions)
        activityLevelAutocompleteTextView.setAdapter(adapter)

        activityLevelAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            activityLevelAutocompleteTextView.setSelection(position)
        }

        bmrAutocompleteTextView =
            view.findViewById(R.id.energy_settings_bmr_autocomplete_textview)
        val bmrOptions = resources.getStringArray(R.array.bmr_options)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, bmrOptions)
        bmrAutocompleteTextView.setAdapter(adapter)

        bmrAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            bmrAutocompleteTextView.setSelection(position)
        }
    }

    override fun onResume() {
        super.onResume()
    }
}