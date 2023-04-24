package com.example.caloriex

import android.content.Context
import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExistingLoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginBtn: SignInButton
    private lateinit var nativeLoginBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_existing_login, container, false)

        auth = Firebase.auth
        loginBtn = view.findViewById(R.id.sign_in_google_button)
        nativeLoginBtn = view.findViewById(R.id.login_button)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        loginBtn.setOnClickListener {
            val intent: Intent = googleSignInClient.signInIntent
            // Start activity for result
            startActivityForResult(intent, 100)
        }

        nativeLoginBtn.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val emailAddress = view.findViewById<EditText>(R.id.login_email_edittext).text
                    val password = view.findViewById<EditText>(R.id.login_password_edittext).text

                    if (emailAddress.isNotEmpty() && password.isNotEmpty()) {
                        auth.signInWithEmailAndPassword(
                            emailAddress.toString(),
                            password.toString()
                        )
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Sign in success", "signInWithEmail:success")
                                    activity?.runOnUiThread {
                                        Toast.makeText(
                                            requireContext(),
                                            "Successfully signed in!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        keepUserLoggedIn()
                                        saveEmail(auth)
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Sign in fail", "signInWithEmail:failure", task.exception)
                                    activity?.runOnUiThread {
                                        Toast.makeText(
                                            requireContext(), "Authentication Error!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Fill out the appropriate fields!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                when (requestCode) {
                    100 -> {
                        // When request code is equal to 100 initialize task
                        val signInAccountTask: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(data)

                        // check condition
                        if (signInAccountTask.isSuccessful) {
                            try {
                                // Initialize sign in account
                                val googleSignInAccount =
                                    signInAccountTask.getResult(ApiException::class.java)

                                if (googleSignInAccount != null) {

                                    val authCredential: AuthCredential =
                                        GoogleAuthProvider.getCredential(
                                            googleSignInAccount.idToken, null
                                        )
                                    // Gets credential from google sign in and uses the credential to sign in to firebase
                                    auth.signInWithCredential(authCredential)
                                        .addOnCompleteListener(requireActivity()) { task ->
                                            // Check condition
                                            if (task.isSuccessful) {
                                                // When task is successful redirect to profile activity
                                                activity?.runOnUiThread {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Successfully signed in!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    keepUserLoggedIn()
                                                    saveEmail(auth)
                                                }
                                            } else {
                                                // When task is unsuccessful display Toast
                                                activity?.runOnUiThread {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Authentication Error!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                }
                            } catch (e: ApiException) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveEmail(auth: FirebaseAuth) {
        // When the user successfully signs in:
        val sharedPreferences = requireActivity().getSharedPreferences("email", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userEmail", auth?.currentUser?.email)
        editor.apply()
    }

    private fun keepUserLoggedIn() {
        // When the user successfully signs in:
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()

        findNavController().navigate(R.id.action_existingLoginFragment_to_messageMotivationFragment)
    }
}
