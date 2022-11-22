package com.jamesjmtaylor.weg.android.subviews

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jamesjmtaylor.weg.android.R

/**
 * Class used for bottomBar currentBackStackEntryAsState()
 */
sealed class BottomBarScreen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val drawableId: Int
) {
    object Land : BottomBarScreen("land", R.string.land, R.drawable.ic_land)
    object Air : BottomBarScreen("air", R.string.air, R.drawable.ic_air)
    object Sea : BottomBarScreen("sea", R.string.sea, R.drawable.ic_sea)
}