package com.learn.firebasechatapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.CircleCropImageBitmap
import com.learn.firebasechatapp.helper.DrawableToBitmap
import com.learn.firebasechatapp.helper.FirebaseUtil
import com.learn.firebasechatapp.signinup.SignUp
import com.learn.firebasechatapp.userinfo.ChangeUserBio
import com.learn.firebasechatapp.userinfo.ChangeUserGender
import com.learn.firebasechatapp.userinfo.ChangeUserPhoneNumber

class OwnProfile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_own_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize Firebase auth
        firebaseAuth = Firebase.auth
        // initialize Firebase database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)

        // initialize custom toolbar and popup menu
        initTopToolbar(view)

        // get user profile's info from firebase and set it to view
        initUserInfoFirebase(view)

        // refresh fragment every 5 seconds
        val refreshHandler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                initUserInfoFirebase(view)
                refreshHandler.postDelayed(this, 1 * 1000)
            }
        }
        refreshHandler.postDelayed(runnable, 1 * 1000)

        // circle user profile picture
        val userProfilePicture: ImageView = view.findViewById(R.id.user_profile_picture)
        val userImageCropped = CircleCropImageBitmap().getRoundedCroppedBitmap(
            DrawableToBitmap().intToBitmap(R.drawable.app_logo, context)!!
        )
        userProfilePicture.setImageBitmap(userImageCropped)

        // bio onclick function
        val userBioLayout: RelativeLayout = view.findViewById(R.id.user_account_bio)
        userBioLayout.setOnClickListener {
            activity!!.startActivity(Intent(activity, ChangeUserBio::class.java))
        }

        // phone number onclick function
        val userPhoneNumberLayout: RelativeLayout = view.findViewById(R.id.user_account_phone)
        userPhoneNumberLayout.setOnClickListener {
            activity!!.startActivity(Intent(activity, ChangeUserPhoneNumber::class.java))
        }

        // gender onclick function
        val userGenderLayout: RelativeLayout = view.findViewById(R.id.user_account_gender)
        userGenderLayout.setOnClickListener {
            activity!!.startActivity(Intent(activity, ChangeUserGender::class.java))
        }
    }

    private fun initUserInfoFirebase(view: View) {
        val user = firebaseAuth.currentUser
        val bio: TextView = view.findViewById(R.id.user_info_detail_bio)
        val username: TextView = view.findViewById(R.id.user_name)
        val email: TextView = view.findViewById(R.id.user_info_detail_email)
        val phoneNumber: TextView = view.findViewById(R.id.user_info_detail_phone)
        val gender: TextView = view.findViewById(R.id.user_info_detail_gender)

        if (user != null) {
            // get data from firebase account info
            username.text = user.displayName
            email.text = user.email

            // get data from firebase database
            firebaseDatabase.reference
                .child("users")
                .child(user.uid)
                .child("bio")
                .get()
                .addOnSuccessListener {
                    bio.text = it.value.toString()
                }
            firebaseDatabase.reference
                .child("users")
                .child(user.uid)
                .child("phone_number")
                .get()
                .addOnSuccessListener {
                    phoneNumber.text = it.value.toString()
                }
            firebaseDatabase.reference
                .child("users")
                .child(user.uid)
                .child("gender")
                .get()
                .addOnSuccessListener {
                    gender.text = it.value.toString()
                }
        }

    }

    private fun initTopToolbar(view: View) {
        // create top toolbar
        val topToolbar: MaterialToolbar = view.findViewById(R.id.top_toolbar)
        topToolbar.inflateMenu(R.menu.user_own_profile_menu)

        topToolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_change_profile_picture -> {
                    Toast.makeText(context, "pic changed", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.action_reset_password -> {
                    Toast.makeText(context, "reset passw", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.action_signout -> {
                    firebaseAuth.signOut()
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT)
                        .show()

                    // go to SignUp activity
                    activity!!.startActivity(Intent(activity, SignUp::class.java))
                    activity!!.finish()
                }
            }
            true
        }
    }
}