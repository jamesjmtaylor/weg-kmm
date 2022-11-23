package com.jamesjmtaylor.weg.android.subviews

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.jamesjmtaylor.weg.android.EQUIPMENT_ID_KEY
import com.jamesjmtaylor.weg.android.EquipmentDetailActivity
import com.jamesjmtaylor.weg.android.EquipmentViewModel
import com.jamesjmtaylor.weg.android.R
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.network.Api
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun EquipmentLazyVerticalGrid(
    searchResultFlow: Flow<PagingData<SearchResult>>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val searchResultListItems: LazyPagingItems<SearchResult> = searchResultFlow.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        items(searchResultListItems.itemCount) { index ->
            val text = searchResultListItems[index]?.title
            EquipmentCard(
                imgUrl = searchResultListItems[index]?.images?.firstOrNull()?.url,
                text = text,
                modifier = Modifier.height(150.dp).clickable {
                    val intent = Intent(context, EquipmentDetailActivity::class.java)
                    val id = searchResultListItems[index]?.id ?: 0
                    intent.putExtra(EQUIPMENT_ID_KEY, id)
                    context.startActivity(intent)
                }
            )
        }
}}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentScreen() {
    WorldwideEquipmentGuideTheme {
        EquipmentLazyVerticalGrid(object : Flow<PagingData<SearchResult>>{
            override suspend fun collect(collector: FlowCollector<PagingData<SearchResult>>) {}
        })
    }
}