package com.luridevlabs.citylights.di

import com.luridevlabs.citylights.data.MonumentsPaging
import com.luridevlabs.citylights.data.monument.remote.mapper.MonumentResponseToMonumentMapper
import com.luridevlabs.citylights.data.monument.MonumentsDataImpl
import com.luridevlabs.citylights.data.monument.remote.MonumentsRemoteImpl
import com.luridevlabs.citylights.data.remote.ApiClient
import com.luridevlabs.citylights.data.remote.CityLightsService
import com.luridevlabs.citylights.domain.MonumentsRepository
import com.luridevlabs.citylights.domain.usecase.GetMonumentPagingListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.presentation.viewmodel.MonumentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
    single<CityLightsService> { ApiClient.retrofit.create(CityLightsService::class.java) }
}

val monumentsModule = module {
    factory { MonumentsRemoteImpl(get()) }
    //factory { MonumentsLocalImpl(get()) }
    factory<MonumentsRepository> { MonumentsDataImpl(get(), get()) }

    factory { MonumentResponseToMonumentMapper() }
    factory { MonumentsPaging(get()) }
    factory { GetMonumentListUseCase(get()) }
    factory { GetMonumentDetailUseCase(get()) }
    factory { GetMonumentPagingListUseCase(get()) }

    viewModel { MonumentsViewModel(get(), get(), get()) }
}