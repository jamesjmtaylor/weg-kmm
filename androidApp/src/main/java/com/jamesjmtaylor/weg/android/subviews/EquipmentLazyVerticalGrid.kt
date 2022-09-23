package com.jamesjmtaylor.weg.android.subviews

import android.app.Application
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
import com.jamesjmtaylor.weg.android.EquipmentViewModel
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.network.Api

@Composable
fun EquipmentLazyVerticalGrid(equipmentState: State<List<SearchResult>?>,
                              modifier: Modifier = Modifier
) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxHeight()
        ) {
            equipmentState.value?.let { searchResults ->
                items(searchResults.count()) { index ->
                    val img = if (searchResults[index].images.first().url.isNullOrEmpty())
                        null
                    else
                        Api.BASE_URL + searchResults[index].images.first().url
                    val text = searchResults[index].title
                    EquipmentCard(
                        imgUrl = img,
                        text = text,
                        modifier = Modifier.height(56.dp)
                    )
                }}
}}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentScreen() {
    WorldwideEquipmentGuideTheme {
        EquipmentLazyVerticalGrid(object : State<List<SearchResult>>{
            override val value: List<SearchResult>
                get() = listOf(SearchResult(),SearchResult())
        })
    }
}