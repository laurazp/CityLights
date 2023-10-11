package com.luridevlabs.citylights.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.R.id.map_menu_item
import com.luridevlabs.citylights.databinding.ActivityMainBinding
import com.luridevlabs.citylights.presentation.fragment.MyListsFragment
import com.luridevlabs.citylights.presentation.fragment.HomeFragment
import com.luridevlabs.citylights.presentation.fragment.MapFragment
import com.luridevlabs.citylights.presentation.fragment.MonumentListFragment
import java.lang.IllegalArgumentException

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMenu()
        loadFragment(HomeFragment())
    }

    private fun setMenu() {
        binding.bnvBottomNavigationBar.setOnItemSelectedListener {item ->
            when (item.itemId) {
                R.id.home_menu_item -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.monuments_menu_item -> {
                    loadFragment(MonumentListFragment())
                    true
                }
                map_menu_item -> {
                    loadFragment(MapFragment())
                    true
                }
                R.id.myLists_menu_item -> {
                    loadFragment(MyListsFragment())
                    true
                }
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}