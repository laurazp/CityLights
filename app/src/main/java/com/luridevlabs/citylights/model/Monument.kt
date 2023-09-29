package com.luridevlabs.citylights.model

data class Monument(
    val monumentId: Long,
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