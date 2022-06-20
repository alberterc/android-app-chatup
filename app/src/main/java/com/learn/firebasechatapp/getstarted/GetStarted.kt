package com.learn.firebasechatapp.getstarted

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.learn.firebasechatapp.R


class GetStarted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // check if this activity needs to be showed to the user or not
        // if the user has already reached the sign-up activity
        // this activity will not be showed (skip to InitialSignUp activity)
        val pref = getSharedPreferences("GetStartedPREF", Context.MODE_PRIVATE)
        if (pref.getBoolean("activity_executed", false)) {
            // go to InitialSignUp activity
            startActivity(Intent(this, InitialSignUp::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        // Get Started onclick button function
        val getStartedButton:Button = findViewById(R.id.get_started_button)
        getStartedButton.setOnClickListener {
            // go to InitialSignUp activity
            startActivity(Intent(applicationContext, InitialSignUp::class.java))
            finish()
        }
    }
}