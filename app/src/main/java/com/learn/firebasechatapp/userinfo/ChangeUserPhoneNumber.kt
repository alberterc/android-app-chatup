package com.learn.firebasechatapp.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.FirebaseUtil

class ChangeUserPhoneNumber : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_phone_number)

        // initialize firebase auth
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        // initialize firebase database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)

        // back button onclick function
        val backButton: ImageView = findViewById(R.id.back_button_top)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // set inputPhoneNumber edittext with saved phone number in firebase database
        val inputPhoneNumber: EditText = findViewById(R.id.change_phone_input)
        firebaseDatabase.reference
            .child("users")
            .child(user!!.uid)
            .child("phone_number")
            .get()
            .addOnSuccessListener {
                if (it.value.toString() == "Unknown") {
                    inputPhoneNumber.hint = "Unknown"
                }
                else {
                    inputPhoneNumber.setText(it.value.toString())
                }
            }

        // save phone number button onclick function
        val savePhoneNumberButton: Button = findViewById(R.id.change_phone_button)
        savePhoneNumberButton.setOnClickListener {
            // change user phone number in firebase database
            // with the string in inputPhoneNumber edittext
            firebaseDatabase.reference
                .child("users")
                .child(user.uid)
                .child("phone_number")
                .setValue(
                    if (inputPhoneNumber.text.toString().trim().isEmpty()) {
                        "Unknown" // set Unknown to database
                    }
                    else {
                        inputPhoneNumber.text.toString() // set this text to database
                    }
                )

            // go back to Own Profile fragment
            onBackPressed()
        }
    }
}