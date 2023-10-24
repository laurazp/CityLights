package com.luridevlabs.citylights.data.personallist.db.mapper

import com.google.android.gms.maps.model.LatLng
import com.luridevlabs.citylights.data.monument.remote.model.ApiMonument
import com.luridevlabs.citylights.data.personallist.db.model.MonumentEntity
import com.luridevlabs.citylights.model.Monument

class MonumentEntityMapper {

    fun mapToDatabase(model: Monument, monumentListId: Long): MonumentEntity {
        return MonumentEntity(
            model.monumentId,
            monumentListId,
            model.title,
            model.description,
            model.style,
            model.address,
            model.hours,
            model.data,
            model.pois,
            model.position.latitude,
            model.position.longitude,
            model.price,
            model.visitInfo,
            model.image,
            model.isFavorite
        )
    }

    fun mapFromDatabase(entity: MonumentEntity): Monument {
        return Monument(
            entity.monumentId,
            entity.title,
            entity.description,
            entity.style,
            entity.address,
            entity.hours,
            entity.data,
            entity.pois,
            LatLng(
                entity.latitude,
                entity.longitude
            ),
            entity.price,
            entity.visitInfo,
            entity.image,
            entity.isFavorite
        )
    }
}