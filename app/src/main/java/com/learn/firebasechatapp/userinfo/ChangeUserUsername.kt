package com.learn.firebasechatapp.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.FirebaseUtil

class ChangeUserUsername : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_username)

        // initialize firebase auth
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        // initialize firebase realtime database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)

        // back button onclick function
        val backButton: ImageView = findViewById(R.id.back_button_top)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // change user username (display name in firebase)
        val usernameInput: EditText = findViewById(R.id.change_username_input)
        val saveButton: Button = findViewById(R.id.change_username_button)
        saveButton.setOnClickListener {
            // set user account display name
            val profileUpdate = userProfileChangeRequest {
                if (usernameInput.text.toString().trim().isNotEmpty()) {
                    displayName = usernameInput.text.toString()
                }
            }
            user!!.updateProfile(profileUpdate)

            // set username in Firebase realtime database
            firebaseDatabase.reference
                .child("users").child(user.uid).child("username")
                .setValue(
                    usernameInput.text.toString().trim().ifEmpty {
                        Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT)
                            .show()
                        user.displayName
                    }
                )

            // go back to OwnProfile fragment
            onBackPressed()
        }
    }
}