package com.jamesjmtaylor.weg.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.models.SearchResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface PreviewEquipmentDetailViewModel {
    val lce: StateFlow<LCE>
    val expandedCardIdsList: StateFlow<List<Int>>
    fun getEquipmentDetails(id: Long)
    fun onCardArrowClicked(cardId: Int)
}

class EquipmentDetailViewModel(private val sdk: EquipmentSDK): PreviewEquipmentDetailViewModel, ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val _lceLiveData = MutableStateFlow(LCE(false))
    override val lce: StateFlow<LCE> = _lceLiveData

    private val _expandedCardIdsList = MutableStateFlow(listOf<Int>())
    override val expandedCardIdsList: StateFlow<List<Int>> get() = _expandedCardIdsList

    override fun getEquipmentDetails(id: Long) {
        backgroundScope.launch{
            _lceLiveData.emit(LCE(true))
            try {
                _lceLiveData.emit(LCE(false, sdk.getEquipmentDetails(id)))
            } catch (exception: Exception) {
                _lceLiveData.emit(LCE(false, error = exception.message))
            }
        }
    }

    override fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also { list ->
            if (list.contains(cardId)) list.remove(cardId) else list.add(cardId)
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
