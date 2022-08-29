package com.jamesjmtaylor.weg.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.jamesjmtaylor.weg.android.subviews.EquipmentCard
import com.jamesjmtaylor.weg.android.subviews.SearchBar
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.network.Api.Companion.BASE_URL

class TabActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val equipmentViewModel = ViewModelProvider(this).get(EquipmentViewModel::class.java)
        setContent {
            WorldwideEquipmentGuideTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EquipmentScreen(equipmentViewModel)
                }
            }
        }
        equipmentViewModel.getEquipment()
    }
}


@Composable
fun EquipmentScreen(vm: EquipmentViewModel,
                    modifier: Modifier = Modifier) {
    val equipmentState = vm.equipmentLiveData.observeAsState()
    equipmentState.value?.count()?.let { Text(text = "Retrieved: $it") }
    Column {
        SearchBar()
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxHeight()
        ) {
            equipmentState.value?.let { searchResults ->
                items(searchResults.count()) { index ->
                    val img = if (searchResults[index].images.first().url.isNullOrEmpty()) null
                    else BASE_URL + searchResults[index].images.first().url
                    EquipmentCard(
                        imgUrl = img,
                        text = searchResults[index].title,
                        modifier = Modifier.height(56.dp)
                    )
                }}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WorldwideEquipmentGuideTheme {
        EquipmentScreen(EquipmentViewModel(LocalContext.current.applicationContext as Application))
    }
}