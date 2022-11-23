package com.jamesjmtaylor.weg.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.models.SearchResult
import kotlinx.coroutines.*

class EquipmentDetailViewModel(private val sdk: EquipmentSDK): ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    //TODO: Update LiveData LCE object
    fun getEquipmentDetails(id: Long) {
        backgroundScope.launch{
            val details = sdk.getEquipmentDetails(id)
            println(details)
        }
    }
    companion object {
        /**
         * The Factory allows injection of parameters into the [EquipmentViewModel] constructor.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory { initializer {
            EquipmentDetailViewModel((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WegApp).sdk)
        }}
    }
}