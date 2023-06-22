package com.jamesjmtaylor.weg.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.models.Contentlet
import kotlinx.coroutines.flow.Flow

/**
 * Serves as a wrapper class for the compose views to retrieve data from the [EquipmentSDK].
 * @param sdk used in as part of the repository pattern.
 * @property equipmentFlow used to paginate the different equipment types.
 */
class EquipmentViewModel(private val sdk: EquipmentSDK): ViewModel() {

    private val _landFlow = sdk.landPagingData
    private val _airFlow = sdk.airPagingData
    private val _seaFlow = sdk.seaPagingData
    var equipmentFlow = EquipmentFlow(_landFlow, _airFlow, _seaFlow)

    data class EquipmentFlow(
        val land: Flow<PagingData<Contentlet>>,
        val air: Flow<PagingData<Contentlet>>,
        val sea: Flow<PagingData<Contentlet>>
    )

    companion object {
        /**
         * The Factory allows injection of parameters into the [EquipmentViewModel] constructor.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory { initializer {
            EquipmentViewModel((this[APPLICATION_KEY] as WegApp).sdk)
        }}
    }
}
