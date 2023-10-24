package com.luridevlabs.citylights.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luridevlabs.citylights.data.personallist.db.dao.ListsDao
import com.luridevlabs.citylights.data.personallist.db.model.MonumentListEntity

@Database(entities = [MonumentListEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listsDao(): ListsDao
}