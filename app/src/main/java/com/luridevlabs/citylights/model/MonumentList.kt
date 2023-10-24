package com.luridevlabs.citylights.model

data class MonumentList(
    var listId: Long,
    val listName: String,
    var monuments: MutableList<Monument>,
)
