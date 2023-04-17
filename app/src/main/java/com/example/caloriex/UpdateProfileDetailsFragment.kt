package com.example.caloriex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UpdateProfileDetailsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var settingsTv: TextView
    private lateinit var ageEt: EditText
    private lateinit var heightEt: EditText
    private lateinit var weightEt: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var sexOptionsAutocompleteTextView: AutoCompleteTextView

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
        ageEt = view.findViewById(R.id.profile_details_age_edittext)
        heightEt = view.findViewById(R.id.profile_details_height_edittext)
        weightEt = view.findViewById(R.id.profile_details_weight_edittext)
        progressBar = view.findViewById(R.id.up_progress_circular)
        settingsTv.text = "Profile Details"

        imageIv.setOnClickListener {
            if (ageEt.text.toString().isNotEmpty() && heightEt.text.toString().isNotEmpty() && weightEt.text.toString().isNotEmpty() && sexOptionsAutocompleteTextView.text.toString().isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                Handler().postDelayed({
                    navController.navigate(R.id.action_updateProfileDetailsFragment_to_settingsFragment)
                }, 1000)
                creatingProfile(ageEt.text.toString().toInt(), heightEt.text.toString().toDouble(), weightEt.text.toString().toDouble(), sexOptionsAutocompleteTextView.text.toString())
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fill out all the required fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeActivity()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        loadData()
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE

        userEmail?.let { encodeEmail(it) }?.let { a ->
            Firebase.database.getReference("profileDetails")
                .child(a)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val profileDetails = dataSnapshot.getValue(ProfileDetails::class.java)
                            ageEt.setText(profileDetails?.age.toString())
                            weightEt.setText(profileDetails?.weight.toString())
                            heightEt.setText(profileDetails?.height.toString())
                            sexOptionsAutocompleteTextView.setText(profileDetails?.sex.toString())
                            creatingProfile(ageEt.text.toString().toInt(), heightEt.text.toString().toDouble(), weightEt.text.toString().toDouble(), sexOptionsAutocompleteTextView.text.toString())

                            userEmail?.let { encodeEmail(it) }?.let { s ->
                                Firebase.database.getReference("energySettings")
                                    .child(s)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                val energy = dataSnapshot.getValue(EnergySettings::class.java)
                                                if (energy?.bmrName.toString() == "Harris Benedict") {
                                                    val hb = calculateBMRHarrisBenedict(
                                                        profileDetails?.sex ?: "",
                                                        profileDetails?.weight ?: 0.0,
                                                        profileDetails?.height ?: 0.0,
                                                        profileDetails?.age ?: 0,
                                                        energy?.activityLevel.toString(),
                                                        energy?.weightGoal.toString().toDouble()
                                                    )
                                                    Firebase.database.reference.child("energyExpenditure")
                                                        .child(encodeEmail(userEmail)).setValue(hb)
                                                } else {
                                                    val msj = calculateBMRMifflinStJeor(
                                                        profileDetails?.sex ?: "",
                                                        profileDetails?.weight ?: 0.0,
                                                        profileDetails?.height ?: 0.0,
                                                        profileDetails?.age ?: 0,
                                                        energy?.activityLevel.toString(),
                                                        energy?.weightGoal.toString().toDouble()
                                                    )
                                                    Firebase.database.reference.child("energyExpenditure")
                                                        .child(encodeEmail(userEmail)).setValue(msj)
                                                }

                                                userEmail?.let { encodeEmail(it) }?.let { t ->
                                                    Firebase.database.getReference("macroRatios")
                                                        .child(t)
                                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    val macroRatios = dataSnapshot.getValue(MacroRatios::class.java)

                                                                    userEmail?.let { encodeEmail(it) }?.let { t ->
                                                                        Firebase.database.getReference("energyExpenditure")
                                                                            .child(t)
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
                                                                                            macroRatios?.proteinRatio ?: 0.0,
                                                                                            macroRatios?.netCarbRatio ?: 0.0,
                                                                                            macroRatios?.fatRatio ?: 0.0
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
                                                                }
                                                            }

                                                            override fun onCancelled(databaseError: DatabaseError) {
                                                                Log.d("databaseError", "$databaseError")
                                                            }
                                                        })
                                                }
                                            } else {
                                                Log.d("dataSnapshot", "No data found")
                                            }
                                            progressBar.visibility = View.GONE
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            Log.d("databaseError", "$databaseError")
                                            progressBar.visibility = View.GONE
                                        }
                                    })
                            }
                            changeActivity()
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

    private fun changeActivity() {
        sexOptionsAutocompleteTextView =
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

    override fun onStop() {
        super.onStop()
        loadData()
    }
}