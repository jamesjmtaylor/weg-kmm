package com.jamesjmtaylor.weg.android.subviews

import android.app.Application
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.jamesjmtaylor.weg.EquipmentType
import com.jamesjmtaylor.weg.android.EquipmentApp
import com.jamesjmtaylor.weg.android.EquipmentViewModel
import com.jamesjmtaylor.weg.android.R
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

@Composable
fun EquipmentBottomNavigation(viewModel: EquipmentViewModel, modifier: Modifier = Modifier) {
    val selectedTab = viewModel.selectedTabLiveData.observeAsState()
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_land),
                    contentDescription = "Land"
                )
            },
            label = {
                Text(stringResource(R.string.land))
            },
            selected = selectedTab.value == EquipmentType.LAND,
            onClick = {viewModel.landTabSelected()}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_air),
                    contentDescription = "Air"
                )
            },
            label = {
                Text(stringResource(R.string.air))
            },
            selected = selectedTab.value == EquipmentType.AIR,
            onClick = {viewModel.airTabSelected()}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_sea),
                    contentDescription = "Sea"
                )
            },
            label = {
                Text(stringResource(R.string.sea))
            },
            selected = selectedTab.value == EquipmentType.SEA,
            onClick = {viewModel.seaTabSelected()}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEquipmentBottomNavigation() {
    WorldwideEquipmentGuideTheme {
        //FIXME: Injecting an AndroidViewModel causes java.lang.ClassCastException when trying to render the preview
        //TODO: Inject LiveData instead?
        EquipmentBottomNavigation(EquipmentViewModel(LocalContext.current.applicationContext as Application))
    }
}