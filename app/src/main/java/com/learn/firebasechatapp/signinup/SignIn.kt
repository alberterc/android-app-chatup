package com.learn.firebasechatapp.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.MainActivity
import com.learn.firebasechatapp.R

class SignIn : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // don't have an account text onclick function
        val createNewAccount: TextView = findViewById(R.id.create_new_account)
        createNewAccount.setOnClickListener{
            startActivity(Intent(applicationContext, SignUp::class.java))
            finish()
        }

        val emailInput: EditText = findViewById(R.id.email_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val signInWithEmailPasswordButton: Button = findViewById(R.id.continue_button)

        // initialize Firebase auth
        firebaseAuth = Firebase.auth

        // user account sign in
        signInWithEmailPasswordButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (email != "" && password != "") {
                signInAccountFirebase(email, password)
            }
        }

    }

    private fun signInAccountFirebase(email: String, password: String) {
        val emailInput: EditText = findViewById(R.id.email_input)
        val passwordInput: EditText = findViewById(R.id.password_input)

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // sign in success, check if email is verified
                if (task.isSuccessful) {
                    checkIfEmailVerified()
                }
                // sign in failed
                else {
                    when ((task.exception as FirebaseAuthException?)!!.errorCode) {
                        "ERROR_INVALID_EMAIL" -> {
                            emailInput.error = "The email address is badly formatted."
                            emailInput.requestFocus()
                        }
                        "ERROR_WRONG_PASSWORD" -> {
                            passwordInput.error = "The password is incorrect."
                            passwordInput.requestFocus()
                            passwordInput.setText("")
                        }
                        "ERROR_USER_DISABLED" -> Toast.makeText(
                            applicationContext,
                            "The user account has been disabled by an administrator.",
                            Toast.LENGTH_LONG
                        ).show()
                        "ERROR_USER_NOT_FOUND" -> Toast.makeText(
                            applicationContext,
                            "Account not found.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun checkIfEmailVerified() {
        val user = firebaseAuth.currentUser
        // email is verified, go to MainActivity activity
        if (user!!.isEmailVerified) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
        // email is not verified, go to VerifyEmail activity
        else {
            startActivity(Intent(applicationContext, VerifyEmail::class.java))
            finish()
            Toast.makeText(applicationContext, "Email not verified", Toast.LENGTH_SHORT)
                .show()
        }
    }
}