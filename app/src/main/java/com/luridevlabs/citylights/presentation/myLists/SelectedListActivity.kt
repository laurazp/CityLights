package com.luridevlabs.citylights.presentation.myLists

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.ActivityMainBinding
import com.luridevlabs.citylights.presentation.fragment.HomeFragment

class SelectedListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSelectedListFragment(SelectedListFragment())
    }

    private fun loadSelectedListFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}