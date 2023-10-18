package com.luridevlabs.citylights.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.luridevlabs.citylights.domain.usecase.GetMonumentPagingListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.domain.usecase.GetPersonalListsUseCase
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.MonumentList
import com.luridevlabs.citylights.presentation.common.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Exception

typealias MonumentListState = ResourceState<List<Monument>>
typealias MonumentDetailState = ResourceState<Monument>
typealias AddPersonalListsState = ResourceState<Void?>
typealias PersonalListsState = ResourceState<List<MonumentList>>

open class MonumentsViewModel (
    private val getMonumentListUseCase: GetMonumentListUseCase,
    private val getMonumentDetailUseCase: GetMonumentDetailUseCase,
    private val getComposeMonumentListUseCase: GetMonumentPagingListUseCase,
    private val getPersonalListsUseCase: GetPersonalListsUseCase
) : ViewModel() {

    private val monumentListMutableLiveData = MutableLiveData<MonumentListState>()
    private val monumentDetailMutableLiveData = MutableLiveData<MonumentDetailState>()
    val monumentsList : Flow<PagingData<Monument>> = getComposeMonumentListUseCase(30)

    private val _addPersonalListMutableLiveData = MutableLiveData<AddPersonalListsState>()
    val addPersonalListMutableLiveData: MutableLiveData<AddPersonalListsState> get() = _addPersonalListMutableLiveData

    private val _personalListsMutableLiveData = MutableLiveData<PersonalListsState>()
    val personalListsMutableLiveData: MutableLiveData<PersonalListsState> get() = _personalListsMutableLiveData

    fun getMonumentListLiveData(): LiveData<MonumentListState> {
        return monumentListMutableLiveData
    }

    fun getMonumentDetailLiveData(): LiveData<MonumentDetailState> {
        return monumentDetailMutableLiveData
    }

    fun getAddPersonalListLiveData(): LiveData<AddPersonalListsState> {
        return addPersonalListMutableLiveData
    }

    fun getPersonalListsLiveData() : LiveData<PersonalListsState> {
        return personalListsMutableLiveData
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
        monumentDetailMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val monument = getMonumentDetailUseCase.execute(monumentId)

                withContext(Dispatchers.Main) {
                    monumentDetailMutableLiveData.value = ResourceState.Success(monument)
                }
            } catch (e: Exception) {
                monumentDetailMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
            }
        }
    }

    fun fetchPersonalLists() {
        //TODO
        _personalListsMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notes = getPersonalListsUseCase.execute()

                withContext(Dispatchers.Main) {
                    _personalListsMutableLiveData.value = ResourceState.Success(notes)
                    _personalListsMutableLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _personalListsMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                    _personalListsMutableLiveData.value = ResourceState.None()
                }
            }
        }
    }

    fun addNewList(listName: String) {
        //TODO
        _addPersonalListMutableLiveData.value = ResourceState.Loading()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                //addListUseCase.execute(list)

                withContext(Dispatchers.Main) {
                    _addPersonalListMutableLiveData.value = ResourceState.Success(null)
                    _addPersonalListMutableLiveData.value = ResourceState.None()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _addPersonalListMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                    _addPersonalListMutableLiveData.value = ResourceState.None()
                }
            }
        }
    }

    fun editList() {
        //TODO
    }

    fun deleteList() {
        //TODO
    }
}
