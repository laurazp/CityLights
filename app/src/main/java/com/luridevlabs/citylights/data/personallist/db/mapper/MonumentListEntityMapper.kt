package com.luridevlabs.citylights.data.personallist.db.mapper

import com.luridevlabs.citylights.data.personallist.db.model.MonumentListEntity
import com.luridevlabs.citylights.model.MonumentList

class MonumentListEntityMapper(
    private val monumentEntityMapper: MonumentEntityMapper
) {

    fun mapToDatabase(model: MonumentList): MonumentListEntity {
        return MonumentListEntity(
            model.listId,
            model.listName,
            model.monuments.map { monumentEntityMapper.mapToDatabase(it, model.listId) }.toMutableList()
        )
    }

    fun mapFromDatabase(entity: MonumentListEntity): MonumentList {
        return MonumentList(
            entity.listId,
            entity.listName,
            entity.monuments.map { monumentEntityMapper.mapFromDatabase(it) }.toMutableList()
        )
    }
}