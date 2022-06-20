package com.learn.firebasechatapp.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.learn.firebasechatapp.R

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val alreadyHaveAccount: TextView = findViewById(R.id.already_have_account)
        alreadyHaveAccount.setOnClickListener{
            val intent = Intent(applicationContext, SignIn::class.java)
            startActivity(intent)
            finish()
        }
    }
}