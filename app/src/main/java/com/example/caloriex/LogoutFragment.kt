package com.example.caloriex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogoutFragment : Fragment() {

    private lateinit var logoutBtn: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var navController: NavController
    private lateinit var navigationView: BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_logout, container, false)
        logoutBtn = view.findViewById(R.id.logout_button)
        navController = findNavController()

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            Auth.GoogleSignInApi.signOut(googleSignInClient.asGoogleApiClient());
            GoogleSignIn.getClient(
                requireContext(),
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut()

            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()

            val ctw = ContextThemeWrapper(requireContext(), R.style.CustomAlertDialogTheme)
            val builder = AlertDialog.Builder(ctw)

            // Set the message show for the Alert time
            builder.setMessage("Are you sure you want to logout?")

            // Set Alert Title
            builder.setTitle("Logout")

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false)

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton("Yes") { dialog, which ->
                Toast.makeText(
                    requireContext(),
                    "Successfully signed out!",
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate(R.id.action_logoutFragment_to_getStartedFragment)
            }

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }

            // Create the Alert dialog
            val alertDialog = builder.create()
            // Show the Alert Dialog box
            alertDialog.show()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        navigationView = view.findViewById(R.id.bottom_navigation)
        navigationView.setupWithNavController(navController)

        // Customize the label visibility mode
        navigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_SELECTED

        // Customize the item selection behavior
        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_diary -> {
                    // Navigate to destination1
                    navController.navigate(R.id.action_settingsFragment_to_dashboardFragment)
                    true
                }

                // Need one for menu_plus

                R.id.menu_charts -> {
                    // Navigate to destination2
                    navController.navigate(R.id.action_settingsFragment_to_chartsFragment)
                    true
                }

                R.id.menu_settings -> {
                    // Navigate to destination2
                    navController.navigate(R.id.logoutFragment)
                    true
                }
                // Add more destinations here...
                else -> false
            }
        }
    }


    override fun onResume() {
        super.onResume()
        // Set the selected item of bottom navigation view when the fragment is resumed
      //  navigationView.menu.findItem(findNavController().currentDestination!!.id).isChecked = true
    }
}
