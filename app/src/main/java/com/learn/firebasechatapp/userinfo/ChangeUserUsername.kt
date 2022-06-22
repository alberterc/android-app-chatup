package com.learn.firebasechatapp.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R

class ChangeUserUsername : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_username)

        // initialize firebase auth
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser

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
                displayName = usernameInput.text.toString()
            }
            user!!.updateProfile(profileUpdate)

            // go back to OwnProfile fragment
            onBackPressed()
        }
    }
}