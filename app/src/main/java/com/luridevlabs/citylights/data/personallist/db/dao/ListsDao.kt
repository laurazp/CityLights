package com.luridevlabs.citylights.data.personallist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luridevlabs.citylights.data.database.DatabaseConstants
import com.luridevlabs.citylights.data.personallist.db.model.MonumentListEntity

@Dao
interface ListsDao {

    @Query("SELECT * FROM ${DatabaseConstants.PERSONAL_LISTS_NAME}")
    fun getLists(): List<MonumentListEntity>

    @Query("SELECT * FROM ${DatabaseConstants.PERSONAL_LISTS_NAME} WHERE id=:listId")
    fun getList(listId: Long): MonumentListEntity

    @Update
    fun editList(list: MonumentListEntity)

    @Insert
    fun addList(list: MonumentListEntity): Long

    @Delete
    fun deleteList(list: MonumentListEntity)
}