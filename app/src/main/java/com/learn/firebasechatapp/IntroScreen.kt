package com.learn.firebasechatapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.learn.firebasechatapp.getstarted.GetStarted

@Suppress("DEPRECATION")
class IntroScreen : AppCompatActivity() {
    private val handler = Handler();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler.postDelayed({
            startActivity(Intent(applicationContext, GetStarted::class.java))
            finish()
        }, 2000)
    }

    override fun onBackPressed() {
        handler.removeCallbacksAndMessages(null)
        finish()
        super.onBackPressed()
    }
}