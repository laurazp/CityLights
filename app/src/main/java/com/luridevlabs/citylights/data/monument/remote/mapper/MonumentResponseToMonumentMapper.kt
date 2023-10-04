package com.luridevlabs.citylights.data.monument.remote.mapper

import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.model.Monument

class MonumentResponseToMonumentMapper {

    fun mapMonument(monument: ApiMonument, isFavorite: Boolean): Monument {
        val mappedMonument = Monument(
            monument.monumentId,
            monument.title,
            monument.description,
            monument.style,
            monument.address,
            monument.hours,
            monument.data,
            monument.pois,
            monument.geometry,
            monument.price,
            monument.visitInfo,
            monument.image,
            isFavorite
        )
        return mappedMonument
    }
}