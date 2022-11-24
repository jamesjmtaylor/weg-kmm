package com.jamesjmtaylor.weg.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.models.SearchResult
import kotlinx.coroutines.*

class EquipmentDetailViewModel(private val sdk: EquipmentSDK): ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val _lceLiveData = MutableLiveData(LCE(false))
    val lceLiveData: LiveData<LCE> = _lceLiveData

    fun getEquipmentDetails(id: Long) {
        _lceLiveData.postValue(LCE(true))
        backgroundScope.launch{
            try {
                _lceLiveData.postValue(LCE(false, sdk.getEquipmentDetails(id)))
            } catch (exception: Exception) {
                _lceLiveData.postValue(LCE(false, error = exception.message))
            }
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

data class LCE(val loading: Boolean, val content: SearchResult? = null, val error: String? = null)