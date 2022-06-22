package com.learn.firebasechatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.learn.firebasechatapp.R

class Friends : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize custom toolbar and popup menu
        initTopToolbar(view)
    }

    private fun initTopToolbar(view: View) {
        // create top toolbar
        val topToolbar: MaterialToolbar = view.findViewById(R.id.top_toolbar)
        topToolbar.inflateMenu(R.menu.friend_list_menu)
        topToolbar.setOnMenuItemClickListener{
            when (it.itemId) {
                R.id.action_chat_list -> {
                    chatListFragment()
                }
            }
            true
        }
    }

    private fun chatListFragment() {
        val fragmentManager: FragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.friend_list_fragment, Chats())
            .commit()
    }

}