package com.example.caloriex

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

class MacroRatiosFragment : Fragment() {

    private lateinit var continueBtn: Button
    private lateinit var proteinEt: EditText
    private lateinit var netCarbsEt: EditText
    private lateinit var fatsEt: EditText
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_macro_ratios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                changeActivity()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        proteinEt = view.findViewById(R.id.protein_edittext)
        netCarbsEt = view.findViewById(R.id.net_carbs_edittext)
        fatsEt = view.findViewById(R.id.fat_edittext)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun changeActivity() {
        continueBtn = view?.findViewById(R.id.macro_ratios_continue_button)!!

        // Waiting for the click event from user. Once done so, this will prompt MessageMotivationFragment
        continueBtn.setOnClickListener {
            if (proteinEt.text.toString().isNotEmpty() && netCarbsEt.text.toString().isNotEmpty() && fatsEt.text.toString().isNotEmpty()) {
                val total = proteinEt.text.toString().toDouble().plus(netCarbsEt.text.toString().toDouble()).plus(fatsEt.text.toString().toDouble())
                if (total == 100.0) {
                    findNavController().navigate(R.id.action_macroRatiosFragment_to_messageMotivationFragment)
                    userEmail?.let { encodeEmail(it) }?.let {
                        Firebase.database.getReference("energyExpenditure")
                            .child(it)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        val energyExp = if (dataSnapshot.value is Long) {
                                            CalorieAmount(dataSnapshot.getValue(Long::class.java)?.toInt())
                                        } else {
                                            dataSnapshot.getValue(CalorieAmount::class.java)
                                        }
                                        val calorie = energyExp?.calories ?: 0
                                        val ratioCalories = calculateMacronutrientRatios(calorie, proteinEt.text.toString().toDouble(), netCarbsEt.text.toString().toDouble(), fatsEt.text.toString().toDouble())
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
                Log.d("macroRatios", "proteinEt is: ${proteinEt.text}")
                Log.d("macroRatios", "netCarbsEt is: ${netCarbsEt.text}")
                Log.d("macroRatios", "fatsEt is: ${fatsEt.text}")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fill out all the required fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        // This makes sure that if user destroys the app and comes back to it, they have to go thru the sign up process all over again in order to ensure there is no input being missed
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
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