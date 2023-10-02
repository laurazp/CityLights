package com.luridevlabs.citylights.presentation.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
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
import com.luridevlabs.citylights.model.ResourceState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentListState
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val monumentsViewModel: MonumentsViewModel by activityViewModel()
    private var markersList: MutableList<MarkerOptions> = mutableListOf()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        return binding.root
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
        this.googleMap = googleMap

        markersList.forEach { marker ->
            googleMap.addMarker(marker)
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

    /*private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        Utils.PERMISSION_ACCESS_BACKGROUND_LOCATION
                    );
                }
            }
        }
    }*/

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

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        //val geocoder = Geocoder(requireContext(), Locale.getDefault())

                        //val list: MutableList<Address>? =
                        //    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        //TODO: ¿?
                        val zaragozaLatLng = LatLng(location.latitude, location.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(zaragozaLatLng))
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG).show()
                //TODO: ¿?
                requestPermissions()
                //val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                //startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }
}
