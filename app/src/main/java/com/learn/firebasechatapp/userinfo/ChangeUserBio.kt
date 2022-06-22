package com.learn.firebasechatapp.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.FirebaseUtil

class ChangeUserBio : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_bio)

        // initialize Firebase auth
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        // initialize Firebase database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)

        // back top button onclick function
        val backButton: ImageView = findViewById(R.id.back_button_top)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // set inputBio edittext with saved bio text in firebase database
        val inputBio: EditText = findViewById(R.id.change_bio_input)
        firebaseDatabase.reference
            .child("users")
            .child(user!!.uid)
            .child("bio")
            .get()
            .addOnSuccessListener {
                inputBio.setText(it.value.toString())
            }

        // save bio button onclick function
        val saveBioButton: Button = findViewById(R.id.change_bio_button)
        saveBioButton.setOnClickListener {
            // change user bio in firebase database
            // with the string in inputBio edittext
            firebaseDatabase.reference
                .child("users")
                .child(user.uid)
                .child("bio")
                .setValue(inputBio.text.toString())

            // go back to Own Profile fragment
            onBackPressed()
        }
    }
}