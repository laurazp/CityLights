package com.luridevlabs.citylights.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "monumentList")
data class MonumentList(
    @PrimaryKey(autoGenerate = true)
    var listId: Int,
    val listName: String,
    var monuments: MutableList<Monument>
)
