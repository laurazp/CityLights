package com.luridevlabs.citylights.data.personallist.db.mapper

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.luridevlabs.citylights.data.personallist.db.model.MonumentEntity

class MonumentListConverter {

    @TypeConverter
    fun fromList(monuments: List<MonumentEntity>): String {
        return Gson().toJson(monuments)
    }

    @TypeConverter
    fun toList(monumentsString: String): List<MonumentEntity> {
        return Gson().fromJson(monumentsString, Array<MonumentEntity>::class.java).toList()
    }

}