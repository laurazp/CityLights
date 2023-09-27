package com.luridevlabs.citylights.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


data class MonumentsResponse(
    @SerializedName("result") val monuments: List<Monument>
)

@Keep
data class Monument(
    @SerializedName("id") val monumentId: Long,
    val title: String,
    val description: String,
    val estilo: String,
    val address: String,
    val horario: String,
    val datacion: String,
    val pois: String,
    val datos: String,
    val price: String,
    val visita: String,
    val image: String,
    val top: String,
    val foursquare: String,
    val lastUpdated: String,
    val geometry: Geometry,
    val links: List<Link>,
    val uri: String,
    val sameAs: String
)

@Keep
data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

@Keep
data class Link(
    val description: String,
    val url: String
)

