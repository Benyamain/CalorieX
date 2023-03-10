package com.example.caloriex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class ExistingLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginBtn: SignInButton
    private lateinit var nativeLoginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existing_login)

        auth = Firebase.auth
        loginBtn = findViewById(R.id.sign_in_google_button)
        nativeLoginBtn = findViewById(R.id.login_button)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.FIREBASE_ID_TOKEN)
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        loginBtn.setOnClickListener {
            val intent: Intent = googleSignInClient.signInIntent
            // Start activity for result
            startActivityForResult(intent, 100)
        }

        nativeLoginBtn.setOnClickListener {
            val emailAddress = findViewById<EditText>(R.id.login_email_edittext).text
            val password = findViewById<EditText>(R.id.login_password_edittext).text

            if (emailAddress.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(emailAddress.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Sign in success", "signInWithEmail:success")
                            Toast.makeText(
                                applicationContext,
                                "Successfully signed in!",
                                Toast.LENGTH_SHORT
                            ).show()
                            keepUserLoggedIn()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Sign in fail", "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed - ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Fill out the appropriate fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
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

                        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                            googleSignInAccount.idToken, null
                        )
                        // Gets credential from google sign in and uses the credential to sign in to firebase
                        auth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) { task ->
                                // Check condition
                                if (task.isSuccessful) {
                                    // When task is successful redirect to profile activity
                                    Toast.makeText(
                                        applicationContext,
                                        "Authentication successful ${auth.currentUser?.uid}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    keepUserLoggedIn()
                                } else {
                                    // When task is unsuccessful display Toast
                                    Toast.makeText(
                                        applicationContext,
                                        "Authentication Failed :" + task.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
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

    private fun keepUserLoggedIn() {
        // When the user successfully signs in:
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
        startActivity(Intent(this, MessageMotivationActivity::class.java))
        finish()
    }
}