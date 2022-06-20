package com.learn.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavBar: BottomNavigationView = findViewById(R.id.nav_bar)

        val navController: NavController = Navigation.findNavController(this, R.id.bottom_nav_bar_fragment_view)
        NavigationUI.setupWithNavController(bottomNavBar, navController)
    }
}