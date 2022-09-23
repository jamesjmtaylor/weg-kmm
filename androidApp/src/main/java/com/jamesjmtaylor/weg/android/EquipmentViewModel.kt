package com.jamesjmtaylor.weg.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.EquipmentType
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.launch

class EquipmentViewModel(app: Application): AndroidViewModel(app) {
    private val sdk = EquipmentSDK(DatabaseDriverFactory(app))
    private val _equipmentLiveData = MutableLiveData<List<SearchResult>>()
    val equipmentLiveData : LiveData<List<SearchResult>> = _equipmentLiveData
    private val _selectedTabLiveData = MutableLiveData<EquipmentType>()
    val selectedTabLiveData : LiveData<EquipmentType> = _selectedTabLiveData

    fun landTabSelected() {
        _selectedTabLiveData.postValue(EquipmentType.LAND)
        viewModelScope.launch {
            _equipmentLiveData.postValue(sdk.getEquipment(EquipmentType.LAND))
        }
    }

    fun airTabSelected() {
        _selectedTabLiveData.postValue(EquipmentType.AIR)
        viewModelScope.launch {
            _equipmentLiveData.postValue(sdk.getEquipment(EquipmentType.AIR))
        }

    }

    fun seaTabSelected() {
        _selectedTabLiveData.postValue(EquipmentType.SEA)
        viewModelScope.launch {
            _equipmentLiveData.postValue(sdk.getEquipment(EquipmentType.SEA))
        }
    }
}
