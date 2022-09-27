package com.jamesjmtaylor.weg.android.subviews

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jamesjmtaylor.weg.android.R
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

@Composable
fun EquipmentCard(
    imgUrl: String?,
    text: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = imgUrl,
            contentDescription = text ?: stringResource(R.string.placeholder_name),
            placeholder = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            color = MaterialTheme.colors.onBackground,
            text = text ?: stringResource(R.string.placeholder_name),
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colors.background)

        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun FavoriteCollectionCardPreview() {
    WorldwideEquipmentGuideTheme {
        EquipmentCard(
            imgUrl = null,
            text = stringResource(R.string.placeholder_name))
    }
}