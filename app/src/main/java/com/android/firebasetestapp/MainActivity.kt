package com.android.firebasetestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.firebasetestapp.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        firebaseAnalytics = Firebase.analytics
        firebaseAuth = Firebase.auth
        setUpListenerForViews()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            showToast("User is already signed in")
        }
    }

    private fun setUpListenerForViews() {
        binding.signInWithEmailPasswordButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty()) {
                showToast("Please write your email")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                showToast("Please write your password")
                return@setOnClickListener
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = firebaseAuth.currentUser
                        Log.d(TAG, "signInWithEmail:success: ${user.toString()}")
                        showToast("Sign in success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        showToast("Authentication failed. ${task.exception?.message}")
                    }
                }
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty()) {
                showToast("Please write your email")
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                showToast("Please write your password")
                return@setOnClickListener
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = firebaseAuth.currentUser
                        Log.d(TAG, "createUserWithEmail:success -> ${user.toString()}")
                        showToast("Sign up success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        showToast("Authentication failed. ${task.exception?.message}")
                    }
                }
        }

        binding.signOutButton.setOnClickListener {
            firebaseAuth.signOut()
        }

        binding.signInWithFirebaseUiButton.setOnClickListener {
            firebaseAnalytics.logEvent("SHERIF") {
                param(FirebaseAnalytics.Param.ITEM_ID, "1234")
                param(FirebaseAnalytics.Param.ITEM_NAME, "sherif")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
            }
            openFirebaseUISignInScreen()
        }
    }

    private fun openFirebaseUISignInScreen() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
//            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.d("Sherif", "User: $user")
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.d("Sherif", "Login Failed")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private val TAG = MainActivity::class.simpleName.toString()
    }
}