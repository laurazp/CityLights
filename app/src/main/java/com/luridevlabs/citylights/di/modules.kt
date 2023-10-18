package com.luridevlabs.citylights.di

import androidx.room.Room
import com.luridevlabs.citylights.data.MonumentsPaging
import com.luridevlabs.citylights.data.database.AppDatabase
import com.luridevlabs.citylights.data.list.ListsDataImpl
import com.luridevlabs.citylights.data.list.local.ListsLocalImpl
import com.luridevlabs.citylights.data.monument.remote.mapper.MonumentResponseToMonumentMapper
import com.luridevlabs.citylights.data.monument.MonumentsDataImpl
import com.luridevlabs.citylights.data.monument.remote.MonumentsRemoteImpl
import com.luridevlabs.citylights.data.remote.ApiClient
import com.luridevlabs.citylights.data.remote.CityLightsService
import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.domain.usecase.GetMonumentPagingListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.domain.usecase.GetPersonalListsUseCase
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
    single<CityLightsService> { ApiClient.retrofit.create(CityLightsService::class.java) }
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "lists_db"
        ).build()
    }
}

val monumentsModule = module {
    factory { MonumentsRemoteImpl(get(), get()) }
    //factory { MonumentsLocalImpl(get()) }
    factory<MonumentsRepository> { MonumentsDataImpl(get()) }

    factory { ListsLocalImpl(get()) }
    factory<MonumentListsRepository> { ListsDataImpl(get()) }

    factory { MonumentResponseToMonumentMapper() }
    factory { MonumentsPaging(get()) }
    factory { GetMonumentListUseCase(get()) }
    factory { GetMonumentDetailUseCase(get()) }
    factory { GetMonumentPagingListUseCase(get()) }
    factory { GetPersonalListsUseCase(get()) }

    viewModel { MonumentsViewModel(get(), get(), get(), get()) }
}