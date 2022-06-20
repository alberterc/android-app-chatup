package com.learn.firebasechatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar

class UserProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_profile)

        val topToolbar: MaterialToolbar = findViewById(R.id.top_toolbar)
        setSupportActionBar(topToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_own_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val signOutButton = item.itemId
        if (signOutButton == R.id.action_signout) {
            Toast.makeText(applicationContext, "Signed out", Toast.LENGTH_SHORT)
                .show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}