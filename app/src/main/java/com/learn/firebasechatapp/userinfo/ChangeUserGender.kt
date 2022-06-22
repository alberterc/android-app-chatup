package com.learn.firebasechatapp.userinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.FirebaseUtil

class ChangeUserGender : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_user_gender)

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

        // set inputGender radiobutton with saved gender in firebase database
        val genderGroup: RadioGroup = findViewById(R.id.change_gender_container)
        firebaseDatabase.reference
            .child("users")
            .child(user!!.uid)
            .child("gender")
            .get()
            .addOnSuccessListener {
                when (it.value.toString()) {
                    "Male" -> genderGroup.check(R.id.gender_male_input)
                    "Female" -> genderGroup.check(R.id.gender_female_input)
                    "Rather not say" -> genderGroup.check(R.id.gender_unknown_input)
                }
            }


        // save gender button onclick function
        val saveGenderButton: Button = findViewById(R.id.change_gender_button)
        saveGenderButton.setOnClickListener {
            val selectedRadioId = genderGroup.checkedRadioButtonId
            val inputGender: RadioButton = findViewById(selectedRadioId)

            // change user gender in firebase database
            firebaseDatabase.reference
                .child("users")
                .child(user.uid)
                .child("gender")
                .setValue(inputGender.text.toString())

            // go back to Own Profile fragment
            onBackPressed()
        }
    }
}