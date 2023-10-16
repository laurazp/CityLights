package com.luridevlabs.citylights.data.list

import com.luridevlabs.citylights.data.list.local.ListsLocalImpl
import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.model.MonumentList

class ListsDataImpl(
    private val listsLocalImpl: ListsLocalImpl
) : MonumentListsRepository {
    override fun getLists(): List<MonumentList> {
        return listsLocalImpl.getLists()
    }

    override fun getList(listId: Int): MonumentList {
        return listsLocalImpl.getList(listId)
    }

    override fun addList(list: MonumentList) {
        listsLocalImpl.addList(list)
    }

    override fun editList(list: MonumentList) {
        listsLocalImpl.editList(list)
    }

    override fun deleteList(list: MonumentList) {
        listsLocalImpl.deleteList(list)
    }
}