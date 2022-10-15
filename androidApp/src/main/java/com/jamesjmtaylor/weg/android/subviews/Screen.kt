package com.jamesjmtaylor.weg.android.subviews

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jamesjmtaylor.weg.android.R

/**
 * Class used for bottomBar currentBackStackEntryAsState()
 */
sealed class Screen(
    val route: String,
    @StringRes val stringId: Int,
    @DrawableRes val drawableId: Int
) {
    object Land : Screen("land", R.string.land, R.drawable.ic_land)
    object Air : Screen("air", R.string.air, R.drawable.ic_air)
    object Sea : Screen("sea", R.string.sea, R.drawable.ic_sea)
}