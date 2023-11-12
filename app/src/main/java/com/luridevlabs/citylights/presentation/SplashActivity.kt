package com.luridevlabs.citylights.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.luridevlabs.citylights.databinding.ActivitySplashBinding
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val monumentsViewModel: MonumentsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            monumentsViewModel.initFavoritesList()
            delay(3000)

            withContext(Dispatchers.Main) {
                startActivity(
                    Intent(this@SplashActivity, MainActivity::class.java)
                )
                finish()
            }
        }
    }
}