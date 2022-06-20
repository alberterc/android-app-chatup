package com.learn.firebasechatapp.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R

class VerifyEmail : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        // initialize Firebase auth
        firebaseAuth = Firebase.auth

        // send first verification email
        sendVerificationEmail()

        // Continue button onclick function
        val continueButton: Button = findViewById(R.id.continue_button)
        continueButton.setOnClickListener {
            firebaseAuth.signOut()
            // go to SignIn activity
            startActivity(Intent(applicationContext, SignIn::class.java))
            finish()
        }

        // Send another verification text onclick function
        val sendVerifyEmail: TextView = findViewById(R.id.verify_email_send)
        sendVerifyEmail.setOnClickListener {
            sendVerificationEmail()
        }
    }

    private fun sendVerificationEmail() {
        val user = firebaseAuth.currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                // email not sent, restart activity
                if (!task.isSuccessful) {
                    Toast.makeText(applicationContext, "Email not sent, check your internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}