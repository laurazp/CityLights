package com.luridevlabs.citylights.data.personallist.db

import com.luridevlabs.citylights.data.database.AppDatabase
import com.luridevlabs.citylights.data.database.DatabaseConstants
import com.luridevlabs.citylights.data.personallist.db.mapper.MonumentListEntityMapper
import com.luridevlabs.citylights.data.personallist.db.model.MonumentListEntity
import com.luridevlabs.citylights.model.MonumentList

class ListsDatabaseImpl(
    private val appDatabase: AppDatabase,
    private val mapper: MonumentListEntityMapper
) {

    fun initFavoriteList() {
        if(appDatabase.listsDao().getLists().isNullOrEmpty()) addList(DatabaseConstants.FAVORITE_LIST_NAME)
    }

    fun getLists(): List<MonumentList> {
        return appDatabase.listsDao().getLists().map { mapper.mapFromDatabase(it) }
    }

    fun getList(listId: Long): MonumentList {
        return mapper.mapFromDatabase(appDatabase.listsDao().getList(listId))
    }

    fun addList(name: String): List<MonumentList> {
        appDatabase.listsDao().addList(MonumentListEntity(listName = name, monuments = mutableListOf()))
        return appDatabase.listsDao().getLists().map { mapper.mapFromDatabase(it) }
    }

    fun editList(list: MonumentList): List<MonumentList> {
        appDatabase.listsDao().editList(mapper.mapToDatabase(list))
        return appDatabase.listsDao().getLists().map { mapper.mapFromDatabase(it) }
    }

    fun deleteList(listId: Long): List<MonumentList> {
        appDatabase.listsDao().deleteList(MonumentListEntity(listId = listId, "", mutableListOf()))
        return appDatabase.listsDao().getLists().map { mapper.mapFromDatabase(it) }
    }
}