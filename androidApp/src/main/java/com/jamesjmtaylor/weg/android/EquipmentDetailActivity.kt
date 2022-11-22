package com.jamesjmtaylor.weg.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

class EquipmentDetailActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: Replace this with EquipmentActivity model instantiation pattern.
        val equipmentDetailViewModel = ViewModelProvider(this)[EquipmentDetailViewModel::class.java]
        setContent { EquipmentDetailScreen() }
    }
}

@Composable
fun EquipmentDetailScreen(modifier: Modifier = Modifier) {
    Text(text = "Hello World", modifier = modifier)
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun PreviewEquipmentDetailScreen() {
    WorldwideEquipmentGuideTheme {
        EquipmentDetailScreen()
    }
}