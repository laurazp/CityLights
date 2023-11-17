package com.luridevlabs.citylights.data.monument.local

import com.luridevlabs.citylights.data.personallist.db.ListsDatabaseImpl
import com.luridevlabs.citylights.model.Monument

class MonumentsDatabaseImpl(
    private val listsDatabaseImpl: ListsDatabaseImpl
) {
    fun isMonumentInFavoritesList(monument: Monument): Boolean {
        var isMonumentInList = false
        listsDatabaseImpl.getLists().forEach {
            isMonumentInList = it.monuments.contains(monument)
        }
        return isMonumentInList
    }
}