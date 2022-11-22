package com.jamesjmtaylor.weg.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
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
        val land: Flow<PagingData<SearchResult>>,
        val air: Flow<PagingData<SearchResult>>,
        val sea: Flow<PagingData<SearchResult>>
    )

    // Define ViewModel factory in a companion object
    companion object {
        /**
         * Used to avoid "java.lang.NoSuchMethodException: com.jamesjmtaylor.weg.android.EquipmentViewModel.<init>"
         * See https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories
         */
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create( modelClass: Class<T>, extras: CreationExtras): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return EquipmentViewModel((application as WegApp).sdk) as T
            }
        }
    }
}
