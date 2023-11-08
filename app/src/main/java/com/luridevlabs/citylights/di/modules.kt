package com.luridevlabs.citylights.di

import androidx.room.Room
import com.luridevlabs.citylights.data.MonumentsPaging
import com.luridevlabs.citylights.data.database.AppDatabase
import com.luridevlabs.citylights.data.database.DatabaseConstants.DATABASE_NAME
import com.luridevlabs.citylights.data.monument.MonumentsDataImpl
import com.luridevlabs.citylights.data.monument.local.MonumentsDatabaseImpl
import com.luridevlabs.citylights.data.monument.remote.MonumentsRemoteImpl
import com.luridevlabs.citylights.data.monument.remote.mapper.MonumentResponseMapper
import com.luridevlabs.citylights.data.personallist.ListsDataImpl
import com.luridevlabs.citylights.data.personallist.db.ListsDatabaseImpl
import com.luridevlabs.citylights.data.personallist.db.mapper.MonumentEntityMapper
import com.luridevlabs.citylights.data.personallist.db.mapper.MonumentListEntityMapper
import com.luridevlabs.citylights.data.remote.ApiClient
import com.luridevlabs.citylights.data.remote.CityLightsService
import com.luridevlabs.citylights.domain.MonumentListsRepository
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.domain.usecase.AddPersonalListUseCase
import com.luridevlabs.citylights.domain.usecase.DeletePersonalListUseCase
import com.luridevlabs.citylights.domain.usecase.EditPersonalListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentPagingListUseCase
import com.luridevlabs.citylights.domain.usecase.GetPersonalListsUseCase
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val baseModule = module {
    single<CityLightsService> { ApiClient.retrofit.create(CityLightsService::class.java) }
}

val monumentsModule = module {
    single(named("appDatabase")) {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().listsDao() }

    factory { MonumentResponseMapper() }
    factory { MonumentEntityMapper() }
    factory { MonumentListEntityMapper(get()) }

    factory { MonumentsDatabaseImpl(get()) }
    factory { MonumentsRemoteImpl(get(), get()) }
    factory { ListsDatabaseImpl(get(named("appDatabase")), get()) }
    factory<MonumentsRepository> { MonumentsDataImpl(get(), get()) }
    factory<MonumentListsRepository> { ListsDataImpl(get()) }

    factory { MonumentsPaging(get()) }
    factory { GetMonumentListUseCase(get()) }
    factory { GetMonumentDetailUseCase(get()) }
    factory { GetMonumentPagingListUseCase(get()) }
    factory { GetPersonalListsUseCase(get()) }
    factory { AddPersonalListUseCase(get()) }
    factory { EditPersonalListUseCase(get()) }
    factory { DeletePersonalListUseCase(get()) }

    viewModel { MonumentsViewModel(get(), get(), get(), get(), get(), get(), get()) }
}