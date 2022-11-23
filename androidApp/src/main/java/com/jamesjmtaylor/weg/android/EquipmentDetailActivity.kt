package com.jamesjmtaylor.weg.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

class EquipmentDetailActivity : ComponentActivity()  {
    private val vm : EquipmentDetailViewModel by viewModels { EquipmentDetailViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id : Long = intent.extras?.getLong(EQUIPMENT_ID_KEY) ?: return
        vm.getEquipmentDetails(id)
        setContent { EquipmentDetailScreen() }
    }
}

const val EQUIPMENT_ID_KEY = "EQUIPMENT_ID_KEY"

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