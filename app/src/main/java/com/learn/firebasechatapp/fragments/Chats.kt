@file:Suppress("DEPRECATION")

package com.learn.firebasechatapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.learn.firebasechatapp.R
import com.learn.firebasechatapp.UserProfile
import com.learn.firebasechatapp.helper.FirebaseUtil
import com.learn.firebasechatapp.model.ChatMessage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class Chats : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var adapter: FirebaseListAdapter<ChatMessage>
    private lateinit var messageList: ListView
    private var itemChatCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize Firebase auth
        firebaseAuth = Firebase.auth
        // initialize Firebase Database
        firebaseDatabase = Firebase.database(FirebaseUtil.firebaseDatabaseURL)

        // create top toolbar
        initTopToolbar(view)

        // send message button onclick function
        val sendMessageButton: FloatingActionButton = view.findViewById(R.id.send_chat_button)
        sendMessageButton.setOnClickListener {
            val user = firebaseAuth.currentUser
            val messageInput: EditText = view.findViewById(R.id.chat_input)

            // send user chat info to database
            firebaseDatabase.reference
                .child("chats")
                .push()
                .setValue(
                    ChatMessage(user!!.uid, user.displayName, messageInput.text.toString())
                )

            // clear edit text for message input
            messageInput.setText("")
        }

        // create message item to show in list view
        messageList = view.findViewById(R.id.chat_list)

        // set adapter options
        val options = FirebaseListOptions.Builder<ChatMessage>()
            .setQuery(firebaseDatabase.reference.child("chats"), ChatMessage::class.java)
            .setLayout(R.layout.item_chat_message)
            .build()

        // set message adapter
        adapter = object : FirebaseListAdapter<ChatMessage> (options) {
            override fun populateView(v: View, model: ChatMessage, position: Int) {
                // Get references to the views of item_chat_message
                val messageProfilePicture: CircleImageView = v.findViewById(R.id.message_profile_picture)
                val messageText: TextView = v.findViewById(R.id.message_text)
                val messageUsername: TextView = v.findViewById(R.id.message_username)
                val messageTime: TextView = v.findViewById(R.id.message_time)

                // Set their text
                messageText.text = model.messageText
                messageUsername.text = model.messageUsername
                firebaseDatabase.reference
                    .child("users").child(model.messageUserId!!).child("profile_picture")
                    .get()
                    .addOnSuccessListener {
                        Picasso.get()
                            .load(it.value.toString())
                            .into(messageProfilePicture)
                    }

                // Format the date before showing it
                messageTime.text = DateFormat.format(
                    "dd-MM-yyyy (HH:mm:ss)",
                    model.messageTime!!
                )

                // user profile picture onclick function
                messageProfilePicture.setOnClickListener {
                    // go to UserProfile activity
                    // send the uid of the clicked profile picture with intent
                    activity!!.startActivity(Intent(context, UserProfile::class.java)
                        .putExtra("uid", model.messageUserId!!))
                }
            }
        }

        messageList.adapter = adapter

        itemChatCount = adapter.count
        // check item chat count from adapter
        // to update list view scroll position (every 250 milliseconds)
        // refresh every 250 milliseconds
        val refreshHandler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (itemChatCount != adapter.count) {
                    messageList.setSelection(adapter.count - 1)
                }
                itemChatCount = adapter.count
                refreshHandler.postDelayed(this, 250)
            }
        }
        refreshHandler.postDelayed(runnable, 250)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun initTopToolbar(view: View) {
        val topToolbar: MaterialToolbar = view.findViewById(R.id.top_toolbar)
        topToolbar.inflateMenu(R.menu.chat_list_menu)
        topToolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_friend_list -> {
                    friendListFragment()
                }

                R.id.action_clear_chat -> {
                    firebaseDatabase.reference
                        .child("chats")
                        .removeValue()
                }
            }
            true
        }
    }

    private fun friendListFragment() {
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.chats_fragment, Friends())
            .commit()
    }

}