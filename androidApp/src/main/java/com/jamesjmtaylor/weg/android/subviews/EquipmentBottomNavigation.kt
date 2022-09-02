package com.jamesjmtaylor.weg.android.subviews

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.jamesjmtaylor.weg.android.R
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

@Composable
fun EquipmentBottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_land),
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.land))
            },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_air),
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.air))
            },
            selected = false,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_sea),
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.sea))
            },
            selected = false,
            onClick = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEquipmentBottomNavigation() {
    WorldwideEquipmentGuideTheme {
        EquipmentBottomNavigation()
    }
}