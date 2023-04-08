package com.example.mygithubuserapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mygithubuserapp.ui.FollowFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String =""

    override fun createFragment(position: Int): Fragment {
        return FollowFragment().apply {
            arguments = Bundle().apply {
                putInt(FollowFragment.ARG_POSITION, position + 1)
                putString(FollowFragment.ARG_USERNAME, username)
            }
        }
    }

    override fun getItemCount(): Int = 2
}