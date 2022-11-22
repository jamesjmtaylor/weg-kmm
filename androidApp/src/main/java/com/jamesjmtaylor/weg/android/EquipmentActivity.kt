package com.jamesjmtaylor.weg.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.*
import com.jamesjmtaylor.weg.android.subviews.EquipmentLazyVerticalGrid
import com.jamesjmtaylor.weg.android.subviews.BottomBarScreen
import com.jamesjmtaylor.weg.android.subviews.SearchBar
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.SearchResult
import com.jamesjmtaylor.weg.network.Api
import kotlinx.coroutines.flow.Flow

class EquipmentActivity : ComponentActivity() {
    private val vm : EquipmentViewModel by viewModels { EquipmentViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { EquipmentScreen(vm.equipmentFlow) }
    }
}

@Composable
fun EquipmentScreen(equipment: EquipmentViewModel.EquipmentFlow) {
    WorldwideEquipmentGuideTheme {// A surface container using the 'background' color from the theme
        val navController = rememberNavController()
        val bottomBarScreens = listOf(BottomBarScreen.Land,BottomBarScreen.Air, BottomBarScreen.Sea)
        Scaffold(//ensures proper layout strategy between topBar, bottomBar, fab, bottomSheet, content, etc.
            //See: https://developer.android.com/jetpack/compose/navigation#bottom-nav
            bottomBar = { BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomBarScreens.forEach { screen ->
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
            NavHost(navController, startDestination = BottomBarScreen.Land.route) {
                composable(BottomBarScreen.Land.route) { EquipmentColumn(equipment.land, padding) }
                composable(BottomBarScreen.Air.route) { EquipmentColumn(equipment.air, padding) }
                composable(BottomBarScreen.Sea.route) { EquipmentColumn(equipment.sea, padding) }
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
        val pagingSource = object : PagingSource<Int, SearchResult>() {
            override fun getRefreshKey(state: PagingState<Int, SearchResult>): Int? = 0
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResult> =
                LoadResult.Page( emptyList(), 0, 1)
        }
        val land = Pager(PagingConfig(pageSize = Api.PAGE_SIZE)) { pagingSource }.flow
        val air = Pager(PagingConfig(pageSize = Api.PAGE_SIZE)) { pagingSource }.flow
        val sea = Pager(PagingConfig(pageSize = Api.PAGE_SIZE)) { pagingSource }.flow
        val equipmentFlow = EquipmentViewModel.EquipmentFlow(land, air, sea)
        EquipmentScreen(equipmentFlow)
    }
}