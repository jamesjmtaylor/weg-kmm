package com.jamesjmtaylor.weg.android.subviews

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.jamesjmtaylor.weg.android.EQUIPMENT_ID_KEY
import com.jamesjmtaylor.weg.android.EquipmentDetailActivity
import com.jamesjmtaylor.weg.android.R
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

@Composable
fun EquipmentLazyVerticalGrid(
    searchResultFlow: Flow<PagingData<SearchResult>>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pagingItems: LazyPagingItems<SearchResult> = searchResultFlow.collectAsLazyPagingItems()
    if (pagingItems.itemCount == 0) Spinner()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(horizontal = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        items(pagingItems.itemCount) { index ->
            val text = pagingItems[index]?.title
            EquipmentCard(
                imgUrl = pagingItems[index]?.images?.firstOrNull()?.url,
                text = text,
                modifier = Modifier.height(150.dp).clickable {
                    val intent = Intent(context, EquipmentDetailActivity::class.java)
                    val id = pagingItems[index]?.id ?: 0
                    intent.putExtra(EQUIPMENT_ID_KEY, id)
                    context.startActivity(intent)
                }
            )
        }

        pagingItems.apply {
            when {
                loadState.append is LoadState.Loading -> item { Spinner() }
                loadState.append is LoadState.Error -> item  { LoadError(loadState.append) }
                loadState.append is LoadState.NotLoading -> item {}
                loadState.refresh is LoadState.Loading -> item { Spinner() }
                loadState.refresh is LoadState.Error -> item { LoadError(loadState.refresh) }
                loadState.refresh is LoadState.NotLoading -> {}
            }
        }
}}

@Composable
fun LoadError(state: LoadState) {
    Text((state as LoadState.Error).error.message ?: stringResource(R.string.default_error))
}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentScreen() {
    WorldwideEquipmentGuideTheme {
        EquipmentLazyVerticalGrid(object : Flow<PagingData<SearchResult>>{
            override suspend fun collect(collector: FlowCollector<PagingData<SearchResult>>) {}
        })
    }
}