package com.luridevlabs.citylights.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.ActivityMainBinding
import com.luridevlabs.citylights.presentation.fragment.FavoritesFragment
import com.luridevlabs.citylights.presentation.fragment.HomeFragment
import com.luridevlabs.citylights.presentation.fragment.MapFragment
import com.luridevlabs.citylights.presentation.fragment.MonumentListFragment
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                R.id.map_menu_item -> {
                    loadFragment(MapFragment())
                    true
                }
                R.id.favorites_menu_item -> {
                    loadFragment(FavoritesFragment())
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