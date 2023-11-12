package com.luridevlabs.citylights.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import com.luridevlabs.citylights.domain.usecase.AddPersonalListUseCase
import com.luridevlabs.citylights.domain.usecase.DeletePersonalListUseCase
import com.luridevlabs.citylights.domain.usecase.EditPersonalListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentDetailUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentListUseCase
import com.luridevlabs.citylights.domain.usecase.GetMonumentPagingListUseCase
import com.luridevlabs.citylights.domain.usecase.GetPersonalListsUseCase
import com.luridevlabs.citylights.domain.usecase.InitFavoriteListUseCase
import com.luridevlabs.citylights.model.Monument
import com.luridevlabs.citylights.model.MonumentList
import com.luridevlabs.citylights.presentation.common.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

typealias MonumentListState = ResourceState<List<Monument>>
typealias MonumentDetailState = ResourceState<Monument>
typealias AddPersonalListsState = ResourceState<Unit?>
typealias PersonalListsState = ResourceState<List<MonumentList>>

open class MonumentsViewModel(
    private val initFavoriteListUseCase: InitFavoriteListUseCase,
    private val getMonumentListUseCase: GetMonumentListUseCase,
    private val getMonumentDetailUseCase: GetMonumentDetailUseCase,
    private val getMonumentPagingListUseCase: GetMonumentPagingListUseCase,
    private val getPersonalListsUseCase: GetPersonalListsUseCase,
    private val addPersonalListUseCase: AddPersonalListUseCase,
    private val editPersonalListUseCase: EditPersonalListUseCase,
    private val deletePersonalListUseCase: DeletePersonalListUseCase,
) : ViewModel() {

    private val monumentListMutableLiveData = MutableLiveData<MonumentListState>()
    private val monumentDetailMutableLiveData = MutableLiveData<MonumentDetailState>()
    val monumentsPagingList: Flow<PagingData<Monument>> = getMonumentPagingListUseCase(30)
    val filteredMonumentList = mutableStateListOf<Monument>()
    var personalLists: List<MonumentList> = mutableStateListOf()
    var selectedListPosition: Int = -1

    private val _addPersonalListMutableLiveData = MutableLiveData<AddPersonalListsState>()
    private val addPersonalListMutableLiveData: MutableLiveData<AddPersonalListsState> get() = _addPersonalListMutableLiveData
    private val personalListsMutableLiveData = MutableLiveData<PersonalListsState>()

    fun getMonumentListLiveData(): LiveData<MonumentListState> {
        return monumentListMutableLiveData
    }

    fun getMonumentDetailLiveData(): LiveData<MonumentDetailState> {
        return monumentDetailMutableLiveData
    }

    fun getAddPersonalListLiveData(): LiveData<AddPersonalListsState> {
        return addPersonalListMutableLiveData
    }

    fun getPersonalListsLiveData(): LiveData<PersonalListsState> {
        return personalListsMutableLiveData
    }

    /**
     * Mantengo esta función fetchMonuments() sin paginado para obtener todos los monumentos
     * de una sola vez y añadirlos al mapa.
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
                withContext(Dispatchers.Main) {
                    monumentListMutableLiveData.value =
                        ResourceState.Error(e.localizedMessage.orEmpty())
                }
            }
        }
    }

    fun fetchMonument(monumentId: String) {
        monumentDetailMutableLiveData.value = ResourceState.Loading()

        try {
            viewModelScope.launch(Dispatchers.IO) {
                val monument = getMonumentDetailUseCase.execute(monumentId)
                withContext(Dispatchers.Main) {
                    monumentDetailMutableLiveData.value = ResourceState.Success(monument)
                }
            }
        } catch (e: Exception) {
            monumentDetailMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
        }
    }

    fun getFilteredMonumentsByName(searchString: String): Flow<PagingData<Monument>> {

        val filteredList = monumentsPagingList.map { pagingData ->
            pagingData.filter { item ->
                item.title.contains(searchString, ignoreCase = true)
            }
        }
        return  filteredList.debounce(200)
    }

     fun sortMonumentsByName(): Flow<PagingData<Monument>> {
        val sortedMonuments = monumentsPagingList
            .map {
                //(it as List<Monument>)
                it.filter { item ->
                    item.title.startsWith("m", true)
                }
            }
         return sortedMonuments
    }

    /*fun <T : Comparable<T>> orderAlphabetically(list: List<T>): List<T> {
        return list.sortedWith { item1, item2 -> item1.compareTo(item2) }
    }*/
    
    fun initFavoritesList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                initFavoriteListUseCase.execute()
            } catch (e: Throwable) {
                Timber.e("ERROR INITIALIZING FAVORITES LIST: $e")
            }
        }
    }

    fun fetchPersonalLists() {
        personalListsMutableLiveData.value = ResourceState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lists = getPersonalListsUseCase.execute()

                withContext(Dispatchers.Main) {
                    personalLists = lists
                    personalListsMutableLiveData.value = ResourceState.Success(lists)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    personalListsMutableLiveData.value =
                        ResourceState.Error(e.localizedMessage.orEmpty())
                }
            }
        }
    }

    fun addNewList(listName: String) {
        personalListsMutableLiveData.value = ResourceState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lists = addPersonalListUseCase.execute(listName)

                withContext(Dispatchers.Main) {
                    personalLists = lists
                    personalListsMutableLiveData.value = ResourceState.Success(lists)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    personalListsMutableLiveData.value =
                        ResourceState.Error(e.localizedMessage.orEmpty())
                }
            }
        }
    }

    private fun editList(list: MonumentList) {
        personalListsMutableLiveData.value = ResourceState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lists = editPersonalListUseCase.execute(list)

                withContext(Dispatchers.Main) {
                    personalLists = lists
                    personalListsMutableLiveData.value = ResourceState.Success(lists)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    personalListsMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                }
            }
        }
    }

    /** Habría que permitir eliminar las listas creadas pero lo dejo para
     * una futura versión si continúo esta app.
     */
    fun deleteList(listId: Long) {
        personalListsMutableLiveData.value = ResourceState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lists = deletePersonalListUseCase.execute(listId)

                withContext(Dispatchers.Main) {
                    personalLists = lists
                    personalListsMutableLiveData.value = ResourceState.Success(lists)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    personalListsMutableLiveData.value = ResourceState.Error(e.localizedMessage.orEmpty())
                }
            }
        }
    }

    fun isMonumentInList(list: MonumentList, monument: Monument): Boolean {
        return list.monuments.contains(monument)
    }

    fun removeMonumentFromList(list: MonumentList, monument: Monument){
        list.monuments.remove(monument)
        editList(list)
    }

    fun addMonumentToList(list: MonumentList, monument: Monument){
        if (list.monuments.none { it.monumentId == monument.monumentId }) {
            list.monuments.add(monument)
            monumentDetailMutableLiveData.value = ResourceState.Success(monument)
            editList(list)

        } else {
            return
        }
    }

    fun getSelectedPersonalList() = personalLists[selectedListPosition]
}
