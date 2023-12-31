package com.luridevlabs.citylights.presentation.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.luridevlabs.citylights.databinding.FragmentMonumentListBinding
import com.luridevlabs.citylights.presentation.main.composables.MainNavigation
import com.luridevlabs.citylights.presentation.theme.CustomAppTheme
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Como la navegación y las clases más globales están realizadas en vista clásica,
 * pero quería implementar también algunas pantallas mediante funciones Composables,
 * he incluido este Fragment como contenedor para la ComposeView.
 */
class MonumentContainerFragment : Fragment() {

    private lateinit var binding: FragmentMonumentListBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonumentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComposeUI()
    }

    private fun initComposeUI() {
        binding.cvListComposeView.setContent {
            CustomAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(monumentsViewModel)
                }
            }
        }
    }
}