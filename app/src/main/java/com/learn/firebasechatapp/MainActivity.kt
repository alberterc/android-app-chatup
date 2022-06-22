package com.learn.firebasechatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.signinup.SignUp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavBar: BottomNavigationView = findViewById(R.id.nav_bar)

        val navController: NavController = Navigation.findNavController(this, R.id.bottom_nav_bar_fragment_view)
        NavigationUI.setupWithNavController(bottomNavBar, navController)
    }
}