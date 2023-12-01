package com.luridevlabs.citylights.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_main_container) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
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
                val item = menu.getItem(i)
                menuItem.actionView?.isActivated = menuItem.itemId == item.itemId
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fcv_main_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun navigateTo(action: Int) {
        if(action == -1) {
            findNavController(R.id.fcv_main_container).popBackStack()
        } else {
            findNavController(R.id.fcv_main_container).navigate(action)
        }
    }
}