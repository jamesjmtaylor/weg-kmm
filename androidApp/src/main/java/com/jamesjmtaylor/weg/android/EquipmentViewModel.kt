package com.jamesjmtaylor.weg.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.EquipmentType
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.network.Api.Companion.PAGE_SIZE
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EquipmentViewModel(app: Application): AndroidViewModel(app) {
    private val sdk = EquipmentSDK(DatabaseDriverFactory(app))

    private val _landFlow: Flow<PagingData<SearchResult>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        SearchResultSource(EquipmentType.LAND, sdk)
    }.flow.cachedIn(viewModelScope)
    private val _airFlow: Flow<PagingData<SearchResult>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        SearchResultSource(EquipmentType.AIR, sdk)
    }.flow.cachedIn(viewModelScope)
    private val _seaFlow: Flow<PagingData<SearchResult>> = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        SearchResultSource(EquipmentType.SEA, sdk)
    }.flow.cachedIn(viewModelScope)
    var selectedFlow = _landFlow

    private val _selectedTabLiveData = MutableLiveData<EquipmentType>()
    val selectedTabLiveData : LiveData<EquipmentType> = _selectedTabLiveData

    fun landTabSelected() {
        _selectedTabLiveData.postValue(EquipmentType.LAND)
        selectedFlow = _landFlow
    }

    fun airTabSelected() {
        _selectedTabLiveData.postValue(EquipmentType.AIR)
        selectedFlow = _airFlow

    }

    fun seaTabSelected() {
        _selectedTabLiveData.postValue(EquipmentType.SEA)
        selectedFlow = _seaFlow
    }
}
