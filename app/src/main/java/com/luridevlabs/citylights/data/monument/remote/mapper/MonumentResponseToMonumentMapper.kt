package com.luridevlabs.citylights.data.monument.remote.mapper

import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.data.monument.remote.model.Geometry
import com.luridevlabs.citylights.model.Monument

class MonumentResponseToMonumentMapper {

    fun mapMonument(monument: ApiMonument, isFavorite: Boolean): Monument {
        val mappedMonument = Monument(
            monument.monumentId,
            monument.title,
            monument.description.orEmpty(),
            monument.style.orEmpty(),
            monument.address.orEmpty(),
            monument.hours.orEmpty(),
            monument.data.orEmpty(),
            monument.pois.orEmpty(),
            monument.geometry ?: Geometry(listOf(0.0, 0.0)),
            monument.price.orEmpty(),
            monument.visitInfo.orEmpty(),
            monument.image.orEmpty(),
            isFavorite
        )
        return mappedMonument
    }
}