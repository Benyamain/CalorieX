package com.example.caloriex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileDetailsFragment : Fragment() {

    private lateinit var continueBtn: Button
    private lateinit var ageEt: EditText
    private lateinit var heightEt: EditText
    private lateinit var weightEt: EditText
    private lateinit var sexOptionsAutocompleteTextView: AutoCompleteTextView
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            changeActivity()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        ageEt = view.findViewById(R.id.age_edittext)
        heightEt = view.findViewById(R.id.height_edittext)
        weightEt = view.findViewById(R.id.weight_edittext)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
    }

    private fun changeActivity() {
        continueBtn = view?.findViewById(R.id.continue_button)!!

        // Waiting for the click event from user. Once done so, this will prompt EnergySettingsActivity
        continueBtn.setOnClickListener {

            if (ageEt.text.toString().isNotEmpty() && heightEt.text.toString().isNotEmpty() && weightEt.text.toString().isNotEmpty() && sexOptionsAutocompleteTextView.text.toString().isNotEmpty()) {
                findNavController().navigate(R.id.action_profileDetailsFragment_to_energySettingsFragment)
                creatingProfile(ageEt.text.toString().toInt(), heightEt.text.toString().toDouble(), weightEt.text.toString().toDouble(), sexOptionsAutocompleteTextView.text.toString())
                Log.d("creatingProfile", "ageEt is: ${ageEt.text}")
                Log.d("creatingProfile", "heightEt is: ${heightEt.text}")
                Log.d("creatingProfile", "weightEt is: ${weightEt.text}")
                Log.d("creatingProfile", "sexOptionsAutocompleteTextView is: ${sexOptionsAutocompleteTextView.text}")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fill out all the required fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        sexOptionsAutocompleteTextView =
            view?.findViewById(R.id.sex_spinner_box_dropdown)!!
        val sexOptions = resources.getStringArray(R.array.sex_options)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, sexOptions)
        sexOptionsAutocompleteTextView.setAdapter(adapter)

        sexOptionsAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            sexOptionsAutocompleteTextView.setSelection(position)
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