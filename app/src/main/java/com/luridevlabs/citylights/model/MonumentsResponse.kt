package com.luridevlabs.citylights.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


data class MonumentsResponse(
    @SerializedName("result") val monuments: List<ApiMonument>
)

@Keep
data class ApiMonument(
    @SerializedName("id") val monumentId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("estilo") val style: String,
    @SerializedName("address") val address: String,
    @SerializedName("horario") val hours: String,
    @SerializedName("datacion") val data: String,
    @SerializedName("pois") val pois: String,
    @SerializedName("price") val price: String,
    @SerializedName("visita") val visitInfo: String,
    @SerializedName("image") val image: String,
    @SerializedName("geometry") val geometry: Geometry
    )

@Keep
data class Geometry(
    val coordinates: List<Double>
)


