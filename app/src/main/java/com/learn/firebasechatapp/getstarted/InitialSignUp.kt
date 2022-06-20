package com.learn.firebasechatapp.getstarted

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.signinup.SignUp

class InitialSignUp : AppCompatActivity() {
    private lateinit var initialSignUpPref: SharedPreferences
    private lateinit var getStartedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        // check if this activity needs to be showed to the user or not
        // if the user has already reached the sign-up activity
        // this activity will not be showed (skip to SignUp activity)
        initialSignUpPref = getSharedPreferences("InitialSignUpPREF", Context.MODE_PRIVATE)
        getStartedPref = getSharedPreferences("GetStartedPREF", Context.MODE_PRIVATE)
        if (initialSignUpPref.getBoolean("activity_executed", false)) {
            // go to SignUp activity
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_sign_up)

        // Already have an account text onclick function
        val alreadyHaveAccount: TextView = findViewById(R.id.already_have_account)
        alreadyHaveAccount.setOnClickListener {
            // update GetStarted and InitialSignUp visited activity
            changeActivityPref()

            // go to SignUp activity
            startActivity(Intent(applicationContext, SignUp::class.java))
            finish()
        }

        // Back button onclick function
        val backButton: ImageView = findViewById(R.id.back_button_top)
        backButton.setOnClickListener {
            onBackPressed() // overridden
        }

        // Create Account button onclick function
        val makeAccountButton: Button = findViewById(R.id.initial_sign_up_button)
        makeAccountButton.setOnClickListener{
            // update GetStarted and InitialSignUp visited activity
            changeActivityPref()

            // go to SignUp activity
            startActivity(Intent(applicationContext, SignUp::class.java))
            finish()
        }
    }

    private fun changeActivityPref() {
        // sign-up activity has been reached
        // InitialSignUp activity will never be showed to the user
        val initialSignUpEdit: SharedPreferences.Editor = initialSignUpPref.edit()
        initialSignUpEdit.putBoolean("activity_executed", true)
        initialSignUpEdit.apply()

        // sign-up activity has been reached
        // GetStarted activity will never be showed to the user
        val getStartedEdit: SharedPreferences.Editor = getStartedPref.edit()
        getStartedEdit.putBoolean("activity_executed", true)
        getStartedEdit.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, GetStarted::class.java))
        finish()
    }
}