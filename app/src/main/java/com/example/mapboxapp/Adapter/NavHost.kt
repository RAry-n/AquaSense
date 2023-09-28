package com.example.mapboxapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mapboxapp.IssueFragment
import com.example.mapboxapp.ReachOut
import com.example.mapboxapp.HomeFragment
import com.example.mapboxapp.homepage
import com.example.mapboxapp.settings


class FragmentBodyAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0-> homepage()
            1-> IssueFragment()
            2-> ReachOut()
            3-> settings()

            else -> HomeFragment()
        }
    }
}