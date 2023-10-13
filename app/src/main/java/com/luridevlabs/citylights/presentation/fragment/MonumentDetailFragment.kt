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
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.databinding.FragmentMonumentDetailBinding
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.composables.MonumentDetail
import com.luridevlabs.citylights.presentation.viewmodel.MonumentDetailState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

//TODO: Eliminar ???
class MonumentDetailFragment : Fragment() {

    private lateinit var binding: FragmentMonumentDetailBinding
    //private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonumentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    /*private fun initContent() {

        monumentsViewModel.getMonumentDetailLiveData().observe(viewLifecycleOwner) { state ->
            if (state != null) handleMonumentDetailState(state)
        }

        if (monumentsViewModel.getMonumentDetailLiveData().value == null) {
            //monumentsViewModel.fetchMonument(args.monumentId)
        }
    }

    private fun handleMonumentDetailState(state: MonumentDetailState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbMonumentDetail.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbMonumentDetail.visibility = View.GONE
                //initUI(state.result)
                initComposeUI(state.result)
                monument = state.result
            }
            is ResourceState.Error -> {
                binding.pbMonumentDetail.visibility = View.GONE
                showErrorDialog(state.error)
            }
        }
    }*/

    /*private fun initUI(monument: Monument) {
        binding.tvMonumentDetailTitle.text = monument.title

        Glide.with(requireContext())
            .load(monument.image)
            .into(binding.ivMonumentImage)

        binding.ibMonumentDetailFavorite.setOnClickListener {
            toggleFavoriteMonument()
        }
    }*/

    private fun toggleFavoriteMonument() {
        /*val isFavorite = monument?.isFavorite ?: false
        monument?.isFavorite = !isFavorite

        binding.ibMonumentDetailFavorite.setImageResource(
            if (monument?.isFavorite == true) {
                R.drawable.baseline_favorite_24
            } else {
                R.drawable.baseline_favorite_border_24
            }
        )*/
    }

   /* private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Reintentar") { dialog, witch ->
                monumentsViewModel.fetchMonuments()
            }
    }*/

}