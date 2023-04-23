package com.example.caloriex

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EnergySettingsFragment : Fragment() {

    private lateinit var continueBtn: Button
    private lateinit var weightGoalEt: EditText
    private lateinit var activityLevelAutocompleteTextView: AutoCompleteTextView
    private lateinit var bmrAutocompleteTextView: AutoCompleteTextView
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_energy_settings, container, false)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                activity?.runOnUiThread {
                    changeActivity(view)
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

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun changeActivity(view: View) {
        continueBtn = view.findViewById(R.id.second_continue_button)

        // Waiting for the click event from user. Once done so, this will prompt MacroRatiosFragment
        continueBtn.setOnClickListener {
            if (bmrAutocompleteTextView.text.toString()
                    .isNotEmpty() && activityLevelAutocompleteTextView.text.toString()
                    .isNotEmpty() && weightGoalEt.text.toString().isNotEmpty()
            ) {
                findNavController().navigate(R.id.action_energySettingsFragment_to_macroRatiosFragment)
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
                                val weight = profileDetails?.weight?.lastIndex?.toDouble() ?: 0.0
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
                                        Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/energyExpenditure").setValue(hb)
                                    } else {
                                        val msj = calculateBMRMifflinStJeor(
                                            sex,
                                            weight,
                                            height,
                                            age,
                                            activityLevelAutocompleteTextView.text.toString(),
                                            weightGoalEt.text.toString().toDouble()
                                        )
                                        Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/energyExpenditure").setValue(msj)
                                    }
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

        weightGoalEt = view.findViewById(R.id.weight_goal_edittext)

        activityLevelAutocompleteTextView =
            view.findViewById(R.id.activity_level_autocomplete_textview)
        val activityLevelOptions = resources.getStringArray(R.array.activity_level_options)

        var adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            activityLevelOptions
        )
        activityLevelAutocompleteTextView.setAdapter(adapter)

        activityLevelAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            activityLevelAutocompleteTextView.setSelection(position)
        }

        bmrAutocompleteTextView =
            view.findViewById(R.id.bmr_autocomplete_textview)
        val bmrOptions = resources.getStringArray(R.array.bmr_options)

        adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, bmrOptions)
        bmrAutocompleteTextView.setAdapter(adapter)

        bmrAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            bmrAutocompleteTextView.setSelection(position)
        }
    }

    override fun onPause() {
        super.onPause()

        // This makes sure that if user destroys the app and comes back to it, they have to go thru the sign up process all over again in order to ensure there is no input being missed
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        Firebase.auth.signOut()
        Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient());
        GoogleSignIn.getClient(
            requireContext(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut()
    }
}