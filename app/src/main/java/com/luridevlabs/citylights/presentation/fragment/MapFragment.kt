package com.luridevlabs.citylights.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luridevlabs.citylights.R
import com.luridevlabs.citylights.databinding.FragmentMapBinding
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()
    private var markersList: MutableList<MarkerOptions> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root

        //TODO: permisos aquí??
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContent()

        val mapFragment = childFragmentManager.findFragmentById(R.id.fcv_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
                binding.pbMapProgressBar.visibility = View.VISIBLE
            }
            is ResourceState.Success -> {
                binding.pbMapProgressBar.visibility = View.GONE
                getMarkersFromData(state.result)
            }
            is ResourceState.Error -> {
                binding.pbMapProgressBar.visibility = View.GONE
                showErrorDialog(state.error)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        markersList.forEach { marker ->
            googleMap.addMarker(marker)

            //.icon(BitmapDescriptorFactory.fromResource(com.luridevlabs.citylights.R.drawable.icon)
        }
    }

    private fun getMarkersFromData(data: List<Monument>) {

        data.forEach { monument ->
            val monumentCoordinates = LatLng(
                monument.geometry?.coordinates?.get(0) ?: 0.0,
                monument.geometry?.coordinates?.get(1) ?: 0.0
            )
            val markerOptions = MarkerOptions()
                .position(monumentCoordinates)
                .title(monument.title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            markersList.add(markerOptions)
        }
    }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(error)
            .setPositiveButton("Aceptar", null)
            .setNegativeButton("Reintentar") { dialog, witch ->
                //TODO: volver a cargar markers
                //monumentsViewModel.fetchMonuments()
            }
    }
}