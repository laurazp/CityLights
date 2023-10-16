package com.luridevlabs.citylights.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.ActivityMainBinding
import com.luridevlabs.citylights.presentation.fragment.PersonalListsFragment
import com.luridevlabs.citylights.presentation.fragment.HomeFragment
import com.luridevlabs.citylights.presentation.fragment.MapFragment
import com.luridevlabs.citylights.presentation.fragment.MonumentListFragment
import java.lang.IllegalArgumentException

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //setMenu()
        //loadFragment(HomeFragment())
        initView()
    }

    private fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main_container) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.monumentListFragment,
                R.id.mapFragment,
                R.id.personalListsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = binding.bnvBottomNavigationBar
        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { menuItem ->
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            for (i in 0 until navView.menu.size()) {
                val menu = navView.menu
                val menuItem = menu.getItem(i)
                menuItem.actionView?.isActivated = menuItem.itemId == menuItem.itemId
            }
            true
        }
    }

    /*private fun setMenu() {
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
                R.id.personalLists_menu_item -> {
                    loadFragment(PersonalListsFragment())
                    true
                }
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }*/

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun navigateTo(action: Int) {
        findNavController(R.id.fcv_main_container).navigate(action)
        findNavController(R.id.fcv_main_container).popBackStack()
    }

    fun setTitle(title: String) {
        val activity = this as AppCompatActivity
        val actionBar = activity.supportActionBar
        actionBar?.title = title
    }
}