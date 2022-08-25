package com.jamesjmtaylor.weg.android.subviews

import android.content.res.Configuration
import android.media.Image
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jamesjmtaylor.weg.android.R
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

@Composable
fun EquipmentCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
        )
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.paddingFromBaseline(
                top = 24.dp, bottom = 8.dp
            )
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun FavoriteCollectionCardPreview() {
    WorldwideEquipmentGuideTheme {
        EquipmentCard(
            drawable = R.drawable.placeholder,
            text = R.string.placeholder_name,
            modifier = Modifier.padding(8.dp))
    }
}