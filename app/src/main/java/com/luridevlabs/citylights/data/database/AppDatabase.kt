package com.luridevlabs.citylights.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luridevlabs.citylights.data.list.local.dao.ListsDao
import com.luridevlabs.citylights.model.MonumentList

@Database(entities = [MonumentList::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun listsDao(): ListsDao
}