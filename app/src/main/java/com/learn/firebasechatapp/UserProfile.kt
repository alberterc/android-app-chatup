@file:Suppress("DEPRECATION")

package com.learn.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.learn.firebasechatapp.helper.FirebaseUtil
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// user profile code template
class UserProfile : AppCompatActivity() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val topToolbar: MaterialToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)

        // get uid from Chats fragment
        uid = intent.getStringExtra("uid").toString()

        // initialize Firebase realtime database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)
        // initialize Firebase storage
        firebaseStorage = Firebase.storage

        // get user profile's info from firebase and set it to view
        initUserInfoFirebase()

        // refresh fragment every 1 seconds
        val refreshHandler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                initUserInfoFirebase()
                refreshHandler.postDelayed(this, 1 * 1000)
            }
        }
        refreshHandler.postDelayed(runnable, 1 * 1000)
    }

    private fun initUserInfoFirebase() {
        val bio: TextView = findViewById(R.id.user_info_detail_bio)
        val username: TextView = findViewById(R.id.user_name)
        val email: TextView = findViewById(R.id.user_info_detail_email)
        val phoneNumber: TextView = findViewById(R.id.user_info_detail_phone)
        val gender: TextView = findViewById(R.id.user_info_detail_gender)
        val profilePicture: CircleImageView = findViewById(R.id.user_profile_picture)

        // get user username from firebase database
        firebaseDatabase.reference
            .child("users")
            .child(uid!!)
            .child("username")
            .get()
            .addOnSuccessListener {
                username.text = it.value.toString()
                if (it.value.toString() == "null") {
                    username.text = "[deleted account]"
                }
            }

        // get user email from firebase database
        firebaseDatabase.reference
            .child("users")
            .child(uid)
            .child("email")
            .get()
            .addOnSuccessListener {
                email.text = it.value.toString()
                if (it.value.toString() == "null") {
                    email.text = "[deleted account]"
                }
            }

        // get user profile picture from database
        firebaseDatabase.reference
            .child("users").child(uid).child("profile_picture")
            .get()
            .addOnSuccessListener { database ->
                if (database.value == null) {
                    firebaseStorage.reference
                        .child("default_assets/profile_picture/user_icon.png")
                        .downloadUrl
                        .addOnSuccessListener {
                            // load user profile picture with default image
                            Picasso.get()
                                .load(it.toString())
                                .into(profilePicture)
                        }
                }
                else {
                    Picasso.get()
                        .load(database.value.toString())
                        .into(profilePicture)
                }
            }

        // get user bio from firebase database
        firebaseDatabase.reference
            .child("users")
            .child(uid)
            .child("bio")
            .get()
            .addOnSuccessListener {
                bio.text = it.value.toString()
                if (it.value.toString() == "null") {
                    bio.text = "[deleted account]"
                }
            }

        // get user phone number from firebase database
        firebaseDatabase.reference
            .child("users")
            .child(uid)
            .child("phone_number")
            .get()
            .addOnSuccessListener {
                phoneNumber.text = it.value.toString()
                if (it.value.toString() == "null") {
                    phoneNumber.text = "[deleted account]"
                }
            }

        // get user gender from firebase database
        firebaseDatabase.reference
            .child("users")
            .child(uid)
            .child("gender")
            .get()
            .addOnSuccessListener {
                gender.text = it.value.toString()
                if (it.value.toString() == "null") {
                    gender.text = "[deleted account]"
                }
            }

        }
}