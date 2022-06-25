@file:Suppress("DEPRECATION")

package com.learn.firebasechatapp.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.FirebaseUtil
import com.learn.firebasechatapp.userinfo.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException


class OwnProfile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage

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
        // initialize Firebase storage
        firebaseStorage = Firebase.storage

        // initialize custom toolbar and popup menu
        initTopToolbar(view)

        // get user profile's info from firebase and set it to view
        initUserInfoFirebase(view)

        // refresh fragment every 1 seconds
        val refreshHandler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                initUserInfoFirebase(view)
                refreshHandler.postDelayed(this, 1 * 1000)
            }
        }
        refreshHandler.postDelayed(runnable, 1 * 1000)

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
        val profilePicture: CircleImageView = view.findViewById(R.id.user_profile_picture)

        if (user != null) {
            // get data from firebase account info
            username.text = user.displayName
            email.text = user.email

            // get user profile picture
            firebaseDatabase.reference
                .child("users").child(user.uid).child("profile_picture")
                .get()
                .addOnSuccessListener {
                    Picasso.get()
                        .load(it.value.toString())
                        .into(profilePicture)
                }

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
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    // requestCode == 100 for profile picture image gallery upload
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
                }
                R.id.action_change_username -> {
                    // go to ChangeUserUsername activity
                    activity!!.startActivity(Intent(activity, ChangeUserUsername::class.java))
                }
                R.id.action_change_password -> {
                    // send a reset password to user's email
                    val user = firebaseAuth.currentUser
                    firebaseAuth.sendPasswordResetEmail(user!!.email!!)
                        .addOnCompleteListener {task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            }
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // requestCode == 100 for profile picture image gallery upload
        if (resultCode == RESULT_OK && requestCode == 100 && data != null && data.data != null) {
            val fileImagePath = data.data!!
            try {
                uploadImageToFirebase(fileImagePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImageToFirebase(imagePath: Uri) {
        val user = firebaseAuth.currentUser
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading Picture")
        progressDialog.show()

        firebaseStorage.reference
            .child("users")
            .child(user!!.uid)
            .child("profile_picture")
            .putFile(imagePath)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()

                    // set user profile picture in Firebase realtime database
                    // set default user profile picture
                    // get default picture from Firebase cloud storage
                    firebaseStorage.reference
                        .child("users/${user.uid}/profile_picture")
                        .downloadUrl
                        .addOnSuccessListener {
                            // set user profile picture in Firebase realtime database
                            firebaseDatabase.reference
                                .child("users").child(user.uid).child("profile_picture")
                                .setValue(it.toString())
                        }
                }
                else {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnProgressListener {
                val uploadProgress: Double = (100.0 * it.bytesTransferred / it.totalByteCount)
                progressDialog.setMessage("Uploaded: ${uploadProgress.toInt()}%")
            }
    }
}