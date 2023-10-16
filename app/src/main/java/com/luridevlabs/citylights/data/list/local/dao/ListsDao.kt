package com.luridevlabs.citylights.data.list.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luridevlabs.citylights.model.MonumentList

@Dao
interface ListsDao {

    @Query("SELECT * FROM monumentList")
    fun getLists(): List<MonumentList>

    @Query("SELECT * FROM monumentList WHERE listId = :listId LIMIT 1")
    fun getList(listId: Int): MonumentList

    @Update
    fun editList(list: MonumentList)

    @Insert
    fun addList(list: MonumentList)

    @Delete
    fun deleteList(list: MonumentList)
}