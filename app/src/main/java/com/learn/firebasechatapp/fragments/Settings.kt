package com.learn.firebasechatapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.signinup.SignUp
import com.learn.firebasechatapp.userinfo.DeleteUserAccount

class Settings : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initialize Firebase auth
        firebaseAuth = Firebase.auth

        super.onViewCreated(view, savedInstanceState)

        // Sign out text onclick function
        val signOutButton: RelativeLayout = view.findViewById(R.id.sign_out_container)
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT)
                .show()

            // go to SignUp activity
            activity!!.startActivity(Intent(activity, SignUp::class.java))
            activity!!.finish()
        }

        // Delete account text onclick function
        val deleteAccountButton: RelativeLayout = view.findViewById(R.id.delete_account_container)
        deleteAccountButton.setOnClickListener {
            // go to DeleteUserAccount activity
            activity!!.startActivity(Intent(activity, DeleteUserAccount::class.java))
        }
    }
}