package com.luridevlabs.citylights.data.list.local

import com.luridevlabs.citylights.data.database.AppDatabase
import com.luridevlabs.citylights.model.MonumentList

class ListsLocalImpl(
    private val appDatabase: AppDatabase
) {

    fun getLists(): List<MonumentList> {
        return appDatabase.listsDao().getLists()
    }

    fun getList(listId: Int): MonumentList {
        return appDatabase.listsDao().getList(listId)
    }

    fun addList(list: MonumentList) {
        appDatabase.listsDao().addList(list)
    }

    fun editList(list: MonumentList) {
        appDatabase.listsDao().editList(list)
    }

    fun deleteList(list: MonumentList) {
        appDatabase.listsDao().deleteList(list)
    }

}