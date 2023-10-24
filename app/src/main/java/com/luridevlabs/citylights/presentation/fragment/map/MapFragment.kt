package com.luridevlabs.citylights.presentation.fragment.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.luridevlabs.citylights.presentation.common.ResourceState.Loading
import com.luridevlabs.citylights.presentation.common.ResourceState.Success
import com.luridevlabs.citylights.presentation.common.ResourceState.Error
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()
    private var googleMap: GoogleMap? = null

    companion object {
        val ZARAGOZA_LOCATION = LatLng(41.65, -0.877)
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

        initContent()

        val mapFragment = childFragmentManager.findFragmentById(R.id.fcv_map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        initContent()
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
            is Loading -> {
                binding.pbMapProgressBar.visibility = View.VISIBLE
            }

            is Success -> {
                binding.pbMapProgressBar.visibility = View.GONE
                getMarkersFromData(state.result)
            }

            is Error -> {
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

                googleMap?.addMarker(markerOptions)
            }
        }
    }


    private fun centerMap(location: LatLng, zoom: Float = 12f) {
        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(location, zoom)
        )
    }

    private fun checkPermissions() {
        when (checkPermission()) {
            PackageManager.PERMISSION_GRANTED -> {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (isLocationEnabled() && location != null) {
                        enableLocationMapButtons()
                        centerMap(LatLng(location.latitude, location.longitude))
                    } else {
                        disabledLocationMapButtons()
                        centerMap(ZARAGOZA_LOCATION)
                    }
                }
            }

            PackageManager.PERMISSION_DENIED -> {
                disabledLocationMapButtons()
                centerMap(ZARAGOZA_LOCATION)
                if (!isLocationDialogShown()) {
                    requestPermission()
                }
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
             * Si se solicita el permiso de ubicación y se concede se vuelven a comprobar para
             * recuperar la localización y centrar el mapa. Si se rechazan se muestra un mensaje
             * de información que permite ir a los ajustes
             **/
            if (granted) {
                checkPermissions()
            } else {
                showInfoPermissionDialog()
            }
        }

    private fun showErrorDialog(error: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.errorTitle)
            .setMessage(error)
            .setPositiveButton(R.string.acceptButtonText) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(R.string.tryAgainButtonText) { dialog, _ ->
                monumentsViewModel.fetchMonuments()
                dialog.dismiss()
            }
            .show()
    }

    private fun showInfoPermissionDialog() {
        setLocationDialogShown()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.locationTitle)
            .setCancelable(false)
            .setMessage(R.string.locationMessage)

            .setPositiveButton(R.string.openSettingsButtonText) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", activity?.packageName, null)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancelButtonText) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    //region MapButtons
    @SuppressLint("MissingPermission")
    private fun enableLocationMapButtons() {
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
    }

    @SuppressLint("MissingPermission")
    private fun disabledLocationMapButtons() {
        googleMap?.isMyLocationEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
    }
    //endregion MapButtons


    //region Preferences
    /** Creo que las preferencias deberían ir en los repositorios de data con Room
     * y Retrofit pero al utilizarse sólo aquí puntualmente me ahorro el flujo de
     * Clean y de casos de uso por avanzar más rápida en el proyecto
     */
    private fun setLocationDialogShown() {
        val sharedPreferences = requireContext().getSharedPreferences("city_lights_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("location_dialog_shown", true)
        editor.apply()
    }

    private fun isLocationDialogShown(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("city_lights_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("location_dialog_shown", false)
    }
    //endregion
}
