package com.luridevlabs.citylights.model

import com.luridevlabs.citylights.data.monument.remote.model.Geometry

data class Monument(
    val monumentId: String,
    val title: String,
    val description: String,
    val style: String,
    val address: String,
    val hours: String,
    val data: String,
    val pois: String,
    val geometry: Geometry,
    val price: String,
    val visitInfo: String,
    val image: String,
    var isFavorite: Boolean = false
)