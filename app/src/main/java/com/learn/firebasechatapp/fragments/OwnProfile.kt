package com.learn.firebasechatapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.helper.CircleCropImageBitmap
import com.learn.firebasechatapp.helper.DrawableToBitmap

class OwnProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_own_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // circle user profile picture
        val userProfilePicture: ImageView = view.findViewById(R.id.user_profile_picture)
        val userImageCropped = CircleCropImageBitmap().getRoundedCroppedBitmap(
            DrawableToBitmap().intToBitmap(R.drawable.app_logo, context)!!
        )
        userProfilePicture.setImageBitmap(userImageCropped)

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
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            true
        }
    }
}