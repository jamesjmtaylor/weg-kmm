package com.jamesjmtaylor.weg.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.jamesjmtaylor.weg.android.subviews.EquipmentBottomNavigation
import com.jamesjmtaylor.weg.android.subviews.EquipmentLazyVerticalGrid
import com.jamesjmtaylor.weg.android.subviews.SearchBar
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

class EquipmentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val equipmentViewModel = ViewModelProvider(this).get(EquipmentViewModel::class.java)
        setContent {
            EquipmentApp(equipmentViewModel)
        }
        equipmentViewModel.landTabSelected()
    }
}

@Composable
fun EquipmentApp(viewModel: EquipmentViewModel) {
    val equipmentState = viewModel.equipmentLiveData.observeAsState()
    WorldwideEquipmentGuideTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            bottomBar = { EquipmentBottomNavigation(viewModel) }
        ) { padding ->
            Column {
                SearchBar()
                EquipmentLazyVerticalGrid(equipmentState, Modifier.padding(padding))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentScreen() {
    WorldwideEquipmentGuideTheme {
        //FIXME: Injecting an AndroidViewModel causes java.lang.ClassCastException when trying to render the preview
        //TODO: Inject LiveData instead?
        EquipmentApp(EquipmentViewModel(LocalContext.current.applicationContext as Application))
    }
}