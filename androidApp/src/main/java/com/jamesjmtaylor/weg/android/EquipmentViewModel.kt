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

/**
 * Serves as a wrapper class for the compose views to retrieve data from the [EquipmentSDK].
 * @param app used to instantiate the [EquipmentSDK].
 * @property equipmentFlow used to paginate the different equipment types.
 */
class EquipmentViewModel(app: Application): AndroidViewModel(app) {
    private val sdk = EquipmentSDK(DatabaseDriverFactory(app))

    private val _landFlow = sdk.pagingData
    private val _airFlow = sdk.pagingData
    private val _seaFlow = sdk.pagingData
    var equipmentFlow = EquipmentFlow(_landFlow, _airFlow, _seaFlow)

    data class EquipmentFlow(
        val land: Flow<PagingData<SearchResult>>,
        val air: Flow<PagingData<SearchResult>>,
        val sea: Flow<PagingData<SearchResult>>
    )
}
