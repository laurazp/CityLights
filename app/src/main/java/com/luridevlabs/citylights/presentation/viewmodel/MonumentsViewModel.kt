package com.luridevlabs.citylights.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentsUseCase
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

typealias MonumentListState = ResourceState<List<Monument>>
typealias MonumentDetailState = ResourceState<Monument>

class MonumentsViewModel (
    private val monumentsUseCase: GetMonumentsUseCase,
    private val monumentsDetailUseCase: GetMonumentDetailUseCase
) : ViewModel() {

    private val monumentMutableLiveData = MutableLiveData<MonumentListState>()
    private val monumentDetailMutableLiveData = MutableLiveData<MonumentDetailState>()

    fun getMonumentLiveData(): LiveData<MonumentListState> {
        return monumentMutableLiveData
    }

    fun getMonumentDetailLiveData(): LiveData<MonumentDetailState> {
        return monumentDetailMutableLiveData
    }

    fun fetchMonuments() {
        monumentMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {

            try {
                //TODO: mirar error (dentro del try falla)
                val data = monumentsUseCase.execute()

                withContext(Dispatchers.Main) {
                    monumentMutableLiveData.value = ResourceState.Success(data)
                }
            } catch (e: Exception) {
                monumentMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
            }
        }
    }

    fun fetchMonument(monumentId: Long) {
        monumentDetailMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = monumentsDetailUseCase.execute(monumentId)

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