package com.learn.firebasechatapp.userinfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.FirebaseUtil
import com.learn.firebasechatapp.signinup.SignUp

class DeleteUserAccount : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user_account)

        // initialize Firebase auth
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser
        // initialize Firebase database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)
        // initialize Firebase storage
        firebaseStorage = Firebase.storage

        // back top button onclick function
        val backButton: ImageView = findViewById(R.id.back_button_top)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // get password for re-authentication
        val passwordInput: EditText = findViewById(R.id.password_input)
        val continueButton: Button = findViewById(R.id.password_button)
        continueButton.setOnClickListener {
            val accountCredential = EmailAuthProvider.getCredential(user!!.email!!, passwordInput.text.toString())
            user.reauthenticate(accountCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        passwordInput.error = null
                        // remove account data from firebase database
                        firebaseDatabase.reference
                            .child("users")
                            .child(user.uid)
                            .removeValue()
                        // remove account data from firebase storage
                        firebaseStorage.reference
                            .child("users")
                            .child(user.uid)
                            .child("profile_picture")
                            .delete()

                        // sign out account
                        // remove account from firebase auth
                        user.delete()
                        firebaseAuth.signOut()
                        // go to SignUp activity
                        startActivity(Intent(this, SignUp::class.java))
                        finishAffinity()
                        Toast.makeText(this, "Account deleted.", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else {
                        passwordInput.error = "The password is incorrect."
                    }
                }
        }
    }
}