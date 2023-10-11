package com.luridevlabs.citylights.model

data class MonumentList(
    val listName: String,
    var monuments: MutableList<Monument>
)
