package com.learn.firebasechatapp.signinup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.MainActivity
import com.learn.firebasechatapp.R


class SignUp : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        // initialize Firebase auth
        firebaseAuth = Firebase.auth

        // check if an account is already signed in
        // if yes, go to MainActivity activity
        // if no, stay here at SignUp activity
        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // already have an account text onclick function
        val alreadyHaveAccount: TextView = findViewById(R.id.already_have_account)
        alreadyHaveAccount.setOnClickListener{
            startActivity(Intent(applicationContext, SignIn::class.java))
            finish()
        }

        val usernameInput: EditText = findViewById(R.id.username_input)
        val emailInput: EditText = findViewById(R.id.email_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val confirmPasswordInput: EditText = findViewById(R.id.confirm_password_input)
        val signUpWithEmailPasswordButton: Button = findViewById(R.id.continue_button)

        // check for invalid password
        passwordInput.setOnFocusChangeListener {
            _, hasFocus ->
                if (!hasFocus) {
                    verifyPassword()
                }
        }

        // check confirm password
        confirmPasswordInput.setOnFocusChangeListener   {
            _, hasFocus ->
                if (!hasFocus) {
                    verifyConfirmPassword()
                }
        }

        // user account sign up
        signUpWithEmailPasswordButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (username != "" && email != "" && password != "" && verifyPassword() && verifyConfirmPassword()) {
                createAccountFirebase(username, email, password)
            }
        }

    }

    private fun createAccountFirebase(username:String, email: String, password: String) {
        val emailInput: EditText = findViewById(R.id.email_input)
        val passwordInput: EditText = findViewById(R.id.password_input)

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // account creation and sign in success
                if (task.isSuccessful) {
                    // set user account display name
                    val user = firebaseAuth.currentUser
                    val profileUpdate = userProfileChangeRequest {
                        displayName = username
                    }
                    user!!.updateProfile(profileUpdate)

                    // go to VerifyEmail activity
                    startActivity(Intent(applicationContext, VerifyEmail::class.java))
                    finish()
                }
                // account creation and sign in failed
                else {
                    when ((task.exception as FirebaseAuthException?)!!.errorCode) {
                        "ERROR_INVALID_EMAIL" -> {
                            emailInput.error = "The email address is badly formatted."
                            emailInput.requestFocus()
                        }
                        "ERROR_EMAIL_ALREADY_IN_USE" -> {
                            emailInput.error = "The email address is already in use by another account."
                            emailInput.requestFocus()
                        }
                        "ERROR_WEAK_PASSWORD" -> {
                            passwordInput.error = "Needs to have at least 6 characters."
                            passwordInput.requestFocus()
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

    private fun verifyPassword(): Boolean {
        val passwordInput: EditText = findViewById(R.id.password_input)
        val passwordStr: String = passwordInput.text.toString()

        // check for password length
        // needs to have at least 6 characters
        if (passwordStr.length < 6) {
            passwordInput.error = "Needs to have at least 6 characters."
            passwordInput.setBackgroundResource(R.drawable.user_text_input_failed_verify_background)
        }
        else {
            passwordInput.setBackgroundResource(R.drawable.user_text_input_background)
        }

        // check for password number
        // needs to have at least 1 number
        if (!passwordStr.contains(".*\\d.*".toRegex()) && passwordStr.length > 5 && passwordStr.contains(".*[A-Za-z].*".toRegex())) {
            passwordInput.error = "Needs to have at least a number."
            passwordInput.setBackgroundResource(R.drawable.user_text_input_failed_verify_background)
        }
        else if (!passwordStr.contains(".*\\d.*".toRegex()) && passwordStr.length < 6  && passwordStr.contains(".*[A-Za-z].*".toRegex())) {
            passwordInput.setBackgroundResource(R.drawable.user_text_input_failed_verify_background)
        }
        else {
            passwordInput.setBackgroundResource(R.drawable.user_text_input_background)
        }

        // check for password letter
        // needs to have at least 1 letter
        if (passwordStr.contains(".*\\d.*".toRegex()) && passwordStr.length > 5 && !passwordStr.contains(".*[A-Za-z].*".toRegex())) {
            passwordInput.error = "Needs to have at least a letter."
            passwordInput.setBackgroundResource(R.drawable.user_text_input_failed_verify_background)
        }
        else if (passwordStr.contains(".*\\d.*".toRegex()) && passwordStr.length < 6  && !passwordStr.contains(".*[A-Za-z].*".toRegex())) {
            passwordInput.setBackgroundResource(R.drawable.user_text_input_failed_verify_background)
        }
        else {
            passwordInput.setBackgroundResource(R.drawable.user_text_input_background)
        }

        // correct password format
        if (passwordStr.length > 5 && passwordStr.contains(".*\\d.*".toRegex()) && passwordStr.contains(".*[A-Za-z].*".toRegex())) {
            passwordInput.error = null
        }

        // only return true if no error is found
        return passwordInput.error == null
    }

    private fun verifyConfirmPassword(): Boolean {
        val passwordInput: EditText = findViewById(R.id.password_input)
        val confirmPasswordInput: EditText = findViewById(R.id.confirm_password_input)

        return if (confirmPasswordInput.text.toString() != passwordInput.text.toString()) {
            confirmPasswordInput.error = "Not the same as the first inputted password."
            confirmPasswordInput.setBackgroundResource(R.drawable.user_text_input_failed_verify_background)
            false
        } else {
            confirmPasswordInput.error = null
            confirmPasswordInput.setBackgroundResource(R.drawable.user_text_input_background)
            true
        }
    }

}