package com.luridevlabs.citylights.domain

import com.luridevlabs.citylights.model.MonumentList

interface MonumentListsRepository {
    fun initFavoriteList()
    fun getPersonalLists(): List<MonumentList>
    fun getPersonalList(listId: Long): MonumentList
    fun addPersonalList(name: String): List<MonumentList>
    fun editPersonalList(list: MonumentList): List<MonumentList>
    fun deletePersonalList(listId: Long): List<MonumentList>
}