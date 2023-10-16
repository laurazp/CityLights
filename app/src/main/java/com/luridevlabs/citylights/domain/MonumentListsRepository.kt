package com.luridevlabs.citylights.domain

import com.luridevlabs.citylights.model.MonumentList

interface MonumentListsRepository {

    fun getLists(): List<MonumentList>

    fun getList(listId: Int): MonumentList

    fun addList(list: MonumentList)

    fun editList(list: MonumentList)

    fun deleteList(list: MonumentList)
}