package com.jamesjmtaylor.weg.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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
fun EquipmentScreen(vm: EquipmentViewModel) {
    val equipmentState = vm.equipmentLiveData.observeAsState()
    equipmentState.value?.count()?.let { Text(text = "Retrieved: $it") }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WorldwideEquipmentGuideTheme {
        EquipmentScreen(EquipmentViewModel(LocalContext.current.applicationContext as Application))
    }
}