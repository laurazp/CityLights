package com.luridevlabs.citylights.data.personallist.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.luridevlabs.citylights.data.database.DatabaseConstants

@Entity(tableName = DatabaseConstants.PERSONAL_LISTS_NAME,
    foreignKeys = [ForeignKey(entity = MonumentListEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("fk_list_id"),
        onDelete = ForeignKey.CASCADE)])

data class MonumentEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val monumentId: Long,
    @ColumnInfo(name = "fk_list_id") val fkPersonalListId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "style") val style: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "hours") val hours: String,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "pois") val pois: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "visit_info") val visitInfo: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean
)