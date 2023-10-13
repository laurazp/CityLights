package com.luridevlabs.citylights.presentation.utils

import java.util.Locale

fun String.capitalizeLowercase(): String {
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}