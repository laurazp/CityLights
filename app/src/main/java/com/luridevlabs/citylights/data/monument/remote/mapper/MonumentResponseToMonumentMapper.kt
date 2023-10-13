package com.luridevlabs.citylights.data.monument.remote.mapper

import com.google.android.gms.maps.model.LatLng
import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.data.monument.remote.model.Geometry
import com.luridevlabs.citylights.model.Monument

class MonumentResponseToMonumentMapper {

    fun mapMonument(monument: ApiMonument): Monument {
        return Monument(
            monument.monumentId.toString(),
            monument.title,
            monument.description.orEmpty(),
            monument.style.orEmpty(),
            monument.address.orEmpty(),
            monument.hours.orEmpty(),
            monument.data.orEmpty(),
            monument.pois.orEmpty(),
            LatLng(
                monument.geometry?.coordinates?.get(1) ?: 0.0,
                monument.geometry?.coordinates?.get(0) ?: 0.0
            ),
            monument.price.orEmpty(),
            monument.visitInfo.orEmpty(),
            monument.image.orEmpty(),
            isFavorite = false
        )
    }
}