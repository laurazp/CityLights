package com.luridevlabs.citylights

import android.app.Application
import com.luridevlabs.citylights.di.baseModule
import com.luridevlabs.citylights.di.monumentsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CityLightsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CityLightsApplication)
            modules(listOf(baseModule, monumentsModule))
            allowOverride(true)
        }
    }
}