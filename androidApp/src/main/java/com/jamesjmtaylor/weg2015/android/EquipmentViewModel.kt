package com.jamesjmtaylor.weg2015.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.jamesjmtaylor.weg2015.EquipmentSDK
import com.jamesjmtaylor.weg2015.EquipmentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class EquipmentViewModel(val sdk: EquipmentSDK) : ViewModel() {
    private var _state = MutableStateFlow<String>("")
    val state: StateFlow<String> get() = _state
    fun getEquipment() {
//        _state = sdk.getEquipmentSearchResults(EquipmentType.LAND, 0)
    }

    // Define ViewModel factory in a companion object
    companion object {
        /**
         * See [create ViewModels with dependencies ](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories)
         */
        @Suppress("UNCHECKED_CAST")
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <EquipmentViewModel : ViewModel> create(
                modelClass: Class<EquipmentViewModel>,
                extras: CreationExtras
            ): EquipmentViewModel {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                return EquipmentViewModel((application as WegApp).sdk) as EquipmentViewModel
            }
        }
    }
}