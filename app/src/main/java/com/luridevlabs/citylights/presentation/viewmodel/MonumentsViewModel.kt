package com.luridevlabs.citylights.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.luridevlabs.citylights.domain.usecase.GetMonumentPagingListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.presentation.common.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

typealias MonumentListState = ResourceState<List<Monument>>
typealias MonumentDetailState = ResourceState<Monument>

open class MonumentsViewModel (
    private val getMonumentListUseCase: GetMonumentListUseCase,
    private val getMonumentDetailUseCase: GetMonumentDetailUseCase,
    private val getComposeMonumentListUseCase: GetMonumentPagingListUseCase
) : ViewModel() {

    private val monumentListMutableLiveData = MutableLiveData<MonumentListState>()
    val monumentDetailMutableLiveData = MutableLiveData<Monument>()
    val monumentsList : Flow<PagingData<Monument>> = getComposeMonumentListUseCase(30)

    fun getMonumentListLiveData(): LiveData<MonumentListState> {
        return monumentListMutableLiveData
    }

    /**
     * Mantengo esta función fetchMonuments() sin paginado para obtener todos los monumentos
     * del tirón y pintarlos en el mapa.
     */
    fun fetchMonuments() {
        monumentListMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = getMonumentListUseCase.execute()

                withContext(Dispatchers.Main) {
                    monumentListMutableLiveData.value = ResourceState.Success(data)
                }
            } catch (e: Exception) {
                monumentListMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
            }
        }
    }

    fun fetchMonument(monumentId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val monument = getMonumentDetailUseCase.execute(monumentId)
            monumentDetailMutableLiveData.value = monument

            withContext(Dispatchers.Main) {

            }

        }
    }
}