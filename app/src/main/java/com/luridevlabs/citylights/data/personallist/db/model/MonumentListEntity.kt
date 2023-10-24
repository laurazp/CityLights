package com.luridevlabs.citylights.data.personallist.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.luridevlabs.citylights.data.database.DatabaseConstants
import com.luridevlabs.citylights.data.personallist.db.mapper.MonumentListConverter

@Entity(tableName = DatabaseConstants.PERSONAL_LISTS_NAME)
@TypeConverters(MonumentListConverter::class)
data class MonumentListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var listId: Long = 0,
    @ColumnInfo(name = "list_name") val listName: String,
    @ColumnInfo(name = "monuments") var monuments: MutableList<MonumentEntity>
)
