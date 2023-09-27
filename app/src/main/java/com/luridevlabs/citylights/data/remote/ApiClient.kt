package com.luridevlabs.citylights.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://www.zaragoza.es/sede/servicio/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}