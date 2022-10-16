package com.jamesjmtaylor.weg.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jamesjmtaylor.weg.EquipmentSDK
import com.jamesjmtaylor.weg.EquipmentType
import com.jamesjmtaylor.weg.android.subviews.EquipmentLazyVerticalGrid
import com.jamesjmtaylor.weg.android.subviews.Screen
import com.jamesjmtaylor.weg.android.subviews.SearchBar
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.network.Api
import com.jamesjmtaylor.weg.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.flow.Flow

class EquipmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val equipmentViewModel = ViewModelProvider(this).get(EquipmentViewModel::class.java)
        setContent { EquipmentApp(equipmentViewModel.equipmentFlow) }
    }
}

@Composable
fun EquipmentApp(equipment: EquipmentViewModel.EquipmentFlow) {
    WorldwideEquipmentGuideTheme {// A surface container using the 'background' color from the theme
        val navController = rememberNavController()
        val screens = listOf(Screen.Land,Screen.Air, Screen.Sea)
        Scaffold(//ensures proper layout strategy between topBar, bottomBar, fab, bottomSheet, content, etc.
            //See: https://developer.android.com/jetpack/compose/navigation#bottom-nav
            bottomBar = { BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(ImageVector.vectorResource(screen.drawableId), stringResource(screen.stringId)) },
                        label = { Text(stringResource(screen.stringId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = { navController.navigate(screen.route) {
                            // avoid building up a large stack of destinations on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true // Avoid multiple copies of the same destination when reselecting the same item
                            restoreState = true // Restore state when reselecting a previously selected item
                        }}
                    )
                }
            }}
        ) { padding ->
            NavHost(navController, startDestination = Screen.Land.route) {
                composable(Screen.Land.route) { EquipmentColumn(equipment.land, padding) }
                composable(Screen.Air.route) { EquipmentColumn(equipment.air, padding) }
                composable(Screen.Sea.route) { EquipmentColumn(equipment.sea, padding) }
            }
        }
    }
}

@Composable
fun EquipmentColumn(equipment: Flow<PagingData<SearchResult>>, padding: PaddingValues) {
    Column {
        SearchBar()
        EquipmentLazyVerticalGrid(equipment, Modifier.padding(padding))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentScreen() {
    WorldwideEquipmentGuideTheme {
        val sdk = EquipmentSDK(DatabaseDriverFactory(LocalContext.current))
        val land = Pager(PagingConfig(pageSize = Api.PAGE_SIZE)) {
            SearchResultSource(EquipmentType.LAND, sdk)
        }.flow
        val air = Pager(PagingConfig(pageSize = Api.PAGE_SIZE)) {
            SearchResultSource(EquipmentType.LAND, sdk)
        }.flow
        val sea = Pager(PagingConfig(pageSize = Api.PAGE_SIZE)) {
            SearchResultSource(EquipmentType.LAND, sdk)
        }.flow
        val equipmentFlow = EquipmentViewModel.EquipmentFlow(land, air, sea)
        EquipmentApp(equipmentFlow)
    }
}