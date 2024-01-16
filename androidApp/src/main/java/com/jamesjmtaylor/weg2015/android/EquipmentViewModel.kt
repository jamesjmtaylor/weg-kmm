package com.jamesjmtaylor.weg2015.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.jamesjmtaylor.weg2015.EquipmentSDK
import com.jamesjmtaylor.weg2015.EquipmentType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquipmentUiState(
    val loading: Boolean = false,
    val equipment: String? = null,
    val error: String? = null
)
class EquipmentViewModel(private val sdk: EquipmentSDK) : ViewModel() {
    private val _uiState = MutableStateFlow(EquipmentUiState())
    val uiState: StateFlow<EquipmentUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null
    /**
     * Enables fetching equipment data in a lifecycle-aware manner.
     * See [expose UI state](https://developer.android.com/topic/architecture/ui-layer#expose-ui-state)
     * for more information.
     */
    fun fetchEquipment() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                _uiState.update { it.copy(loading = true) }
                val equipment = sdk.getEquipmentSearchResults(EquipmentType.LAND, 0)
                _uiState.update { it.copy(loading = false, equipment = equipment.first().toString())  }
            } catch (exception: Exception) {
                _uiState.update { it.copy(loading = false, error = exception.message) }
            }
        }
    }

    // Define ViewModel factory in a companion object
    companion object {
        /**
         * Enables the injection of custom properties into the [ViewModel].
         * See [create ViewModels with dependencies](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories)
         * for more information.
         */
        @Suppress("UNCHECKED_CAST")
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <EquipmentViewModel : ViewModel> create(
                modelClass: Class<EquipmentViewModel>,
                extras: CreationExtras
            ): EquipmentViewModel {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                return EquipmentViewModel((application as WegApp).sdk) as EquipmentViewModel
            }
        }
    }
}