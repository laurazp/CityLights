package com.luridevlabs.citylights.model

import com.google.android.gms.maps.model.LatLng

data class Monument(
    val monumentId: Long,
    val title: String,
    val description: String,
    val style: String,
    val address: String,
    val hours: String,
    val data: String,
    val pois: String,
    val position: LatLng,
    val price: String,
    val visitInfo: String,
    val image: String,
    var isFavorite: Boolean = false
)