package com.jamesjmtaylor.weg.android

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.EquipmentType
import com.jamesjmtaylor.weg.models.SearchResult

class SearchResultSource(val equipmentType: EquipmentType,
                      val equipmentSDK: EquipmentSDK) : PagingSource<Int, SearchResult>() {
    override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? {
        return state.anchorPosition
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> {
        val nextPage = params.key ?: 0
        val searchResultList = equipmentSDK.getEquipment(equipmentType, nextPage) ?: emptyList()
        return LoadResult.Page(
            data = searchResultList,
            prevKey = if (nextPage == 0) null else nextPage - 1,
            nextKey = if (searchResultList.isEmpty()) null else nextPage + 1
        )
    }
}