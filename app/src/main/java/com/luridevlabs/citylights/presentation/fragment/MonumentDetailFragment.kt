package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentMonumentDetailBinding
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentDetailState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MonumentDetailFragment : Fragment() {

    private val binding: FragmentMonumentDetailBinding by lazy {
        FragmentMonumentDetailBinding.inflate(layoutInflater)
    }

    private val args: MonumentDetailFragmentArgs by navArgs()

    private val monumentsViewModel: MonumentsViewModel by activityViewModel()

    private var monument: Monument? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        monumentsViewModel.fetchMonument(args.monumentId)
    }

    private fun initViewModel() {

        monumentsViewModel.getMonumentDetailLiveData().observe(viewLifecycleOwner) { state ->
            handleMonumentDetailState(state)
        }
    }

    private fun handleMonumentDetailState(state: MonumentDetailState) {
        when(state) {
            is ResourceState.Loading -> {
                binding.pbMonumentDetail.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbMonumentDetail.visibility = View.GONE
                initUI(state.result)
            }
            is ResourceState.Error -> {
                binding.pbMonumentDetail.visibility = View.GONE
                showErrorDialog(state.error)
            }
        }
    }

    private fun initUI(monument: Monument) {
        binding.tvMonumentDetailTitle.text = monument.title
        //TODO: add all the fields

        Glide.with(requireContext())
            .load(monument.image)
            .into(binding.ivMonumentImage)

        binding.ibMonumentDetailFavorite.setOnClickListener {
            toggleFavoriteMonument()
        }
    }

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