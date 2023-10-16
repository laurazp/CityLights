package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.databinding.FragmentMonumentListBinding
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.composables.CircularProgressBar
import com.luridevlabs.citylights.presentation.composables.Navigation
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Como la navegación y las clases más globales están realizadas en vista clásica
 * pero quería implementar también algunas pantallas mediante funciones Composables,
 * he incluido este Fragment como contenedor para la ComposeView donde los implementaré.
 */
class MonumentListFragment : Fragment() {

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

        initContent()
        //initComposeUI()
    }

    private fun initContent() {
        monumentsViewModel.getMonumentListLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleMonumentListState(state)
        }

        if (monumentsViewModel.getMonumentListLiveData().value == null) {
            monumentsViewModel.fetchMonuments()
        }
    }

    private fun handleMonumentListState(state: MonumentListState) {
        when(state) {
            is ResourceState.Loading -> {
                //binding.pbMonumentList.visibility = View.VISIBLE
                binding.cvListComposeView.setContent {
                    MaterialTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            CircularProgressBar()
                        }
                    }
                }
            }
            is ResourceState.Success -> {
                //binding.pbMonumentList.visibility = View.GONE
                initComposeUI()
                //monumentListAdapter.submitList(state.result)
            }
            is ResourceState.Error -> {
                //binding.pbMonumentList.visibility = View.GONE
                showErrorDialog(state.error)
            }
            else -> {}
        }
    }

    private fun initComposeUI() {
        binding.cvListComposeView.setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Reintentar") { dialog, witch ->
                monumentsViewModel.fetchMonuments()
            }
    }
}