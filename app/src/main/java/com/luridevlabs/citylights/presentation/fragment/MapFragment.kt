package com.luridevlabs.citylights.presentation.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
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
import com.luridevlabs.citylights.presentation.common.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()
    private var markersList: MutableList<MarkerOptions> = mutableListOf()
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (checkPermission()) {
            PackageManager.PERMISSION_GRANTED -> {
                if (!isLocationEnabled()) {
                    //deniedMapButtons()
                    //clearMap()
                } else {
                    //activateMapButtons()
                    //clearMap()
                }
            }

            PackageManager.PERMISSION_DENIED -> {
                requestPermission()
                //TODO: revisar
                /*if (checkPermissionDialog) {
                    requestPermission()
                }*/
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //checkPermission()

        initContent()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fcv_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //centerMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        markersList.forEach { marker ->
            googleMap.addMarker(marker)
        }

        centerMap()

        /*if (checkPermissions() && isLocationEnabled()) {
            //TODO: show user location
            println("location enabled")
        } else {
            println("location disabled")
            googleMap.setLatLngBoundsForCameraTarget(
                LatLngBounds(
                    LatLng(41.65, -0.877),
                    LatLng(41.65, -0.877)
                )
            )
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(13f)) //moveCamera
        }*/
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
        when (state) {
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

    private fun getMarkersFromData(data: List<Monument>) {

        data.forEach { monument ->
            if (monument.position != LatLng(0.0, 0.0)) {
                val markerOptions = MarkerOptions()
                    .position(monument.position)
                    .title(monument.title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))

                markersList.add(markerOptions)
            }
        }
    }

    private fun centerMap() {
        when (checkPermission()) {
            PackageManager.PERMISSION_GRANTED -> {
                //var currentLatLng: LatLng
                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (!isLocationEnabled() || location == null) {
                        //TODO: deniedMapButtons()
                        googleMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(41.65, -0.877), //Coordenadas del centro de Zaragoza
                                13f
                            )
                        )
                    } else {
                        //TODO: activateMapButtons()
                        googleMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                6f
                            )
                        )
                    }
                }
            }

            PackageManager.PERMISSION_DENIED -> {
                //TODO: deniedMapButtons()
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(41.65, -0.877), //Coordenadas del centro de Zaragoza
                        13f
                    )
                )
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermission(): Int {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermission() {
        geoLocationRequestContract.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val geoLocationRequestContract =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            /**
             * We just send message when permission is not accepted show this message always,
             * except denied for ever
             **/
            if (!granted) {
                //TODO: showInfoPermissionDialog()
            }
        }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.errorTitle)
            .setMessage(error)
            .setPositiveButton(R.string.acceptButtonText, null)
            .setNegativeButton(R.string.tryAgainButtonText) { dialog, _ ->
                monumentsViewModel.fetchMonuments()
                //TODO: cerrar diálogo ??
            }
    }

    private fun showLocationDisabledDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.locationTitle)
            .setMessage(R.string.locationMessage)
            .setPositiveButton(R.string.acceptButtonText, null)
    }
}
