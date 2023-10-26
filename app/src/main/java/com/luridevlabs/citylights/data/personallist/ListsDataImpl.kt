package com.luridevlabs.citylights.data.personallist

import com.luridevlabs.citylights.data.personallist.db.ListsDatabaseImpl
import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.model.MonumentList

class ListsDataImpl(
    private val listsDatabaseImpl: ListsDatabaseImpl,
) : MonumentListsRepository {

    override fun getPersonalLists(): List<MonumentList> {
        return listsDatabaseImpl.getLists()
    }

    override fun getPersonalList(listId: Long): MonumentList {
        return listsDatabaseImpl.getList(listId)
    }

    override fun addPersonalList(name: String): List<MonumentList> {
        return listsDatabaseImpl.addList(name)
    }

    override fun editPersonalList(list: MonumentList): List<MonumentList> {
        return listsDatabaseImpl.editList(list)
    }

    override fun deletePersonalList(listId: Long): List<MonumentList> {
        return listsDatabaseImpl.deleteList(listId)
    }
}