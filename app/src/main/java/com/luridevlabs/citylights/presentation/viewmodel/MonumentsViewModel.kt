package com.luridevlabs.citylights.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.luridevlabs.citylights.domain.usecase.GetComposeMonumentListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

typealias MonumentListState = ResourceState<List<Monument>>
typealias MonumentDetailState = ResourceState<Monument>

open class MonumentsViewModel (
    private val getMonumentListUseCase: GetMonumentListUseCase,
    private val getMonumentDetailUseCase: GetMonumentDetailUseCase,
    private val getComposeMonumentListUseCase: GetComposeMonumentListUseCase
) : ViewModel() {

    private val monumentListMutableLiveData = MutableLiveData<MonumentListState>()
    private val monumentDetailMutableLiveData = MutableLiveData<MonumentDetailState>()
    val monumentsList : Flow<PagingData<Monument>> = getComposeMonumentListUseCase(30)

    fun getMonumentListLiveData(): LiveData<MonumentListState> {
        return monumentListMutableLiveData
    }

    fun getMonumentDetailLiveData(): LiveData<MonumentDetailState> {
        return monumentDetailMutableLiveData
    }

    fun fetchMonuments() {
        monumentListMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = getMonumentListUseCase.execute()

                data.forEach { monument ->
                    println(monument.title)
                    println(monument.image)
                    println("------------")
                }

                withContext(Dispatchers.Main) {
                    monumentListMutableLiveData.value = ResourceState.Success(data)
                }
            } catch (e: Exception) {
                monumentListMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
            }
        }
    }

    fun fetchMonument(monumentId: Long) {
        monumentDetailMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = getMonumentDetailUseCase.execute(monumentId)

                withContext(Dispatchers.Main) {

                    monumentDetailMutableLiveData.value = ResourceState.Success(data)
                }
            } catch (e: Exception) {
                monumentDetailMutableLiveData.value =
                    ResourceState.Error(e.localizedMessage.orEmpty())
            }
        }
    }
}