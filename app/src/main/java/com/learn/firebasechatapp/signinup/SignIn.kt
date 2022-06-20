package com.learn.firebasechatapp.signinup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.learn.firebasechatapp.R

class SignIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val createNewAccount: TextView = findViewById(R.id.create_new_account)
        createNewAccount.setOnClickListener{
            val intent = Intent(applicationContext, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }
}