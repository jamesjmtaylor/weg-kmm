package com.jamesjmtaylor.weg.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.jamesjmtaylor.weg.android.subviews.Spinner
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

class EquipmentDetailActivity : ComponentActivity()  {
    private val vm : EquipmentDetailViewModel by viewModels { EquipmentDetailViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id : Long = intent.extras?.getLong(EQUIPMENT_ID_KEY) ?: return
        vm.getEquipmentDetails(id)
        setContent { EquipmentDetailScreen(vm.lceLiveData) }
    }
}

const val EQUIPMENT_ID_KEY = "EQUIPMENT_ID_KEY"

@Composable
fun EquipmentDetailScreen(lceLiveData: LiveData<LCE>, modifier: Modifier = Modifier) {
    val lceState = lceLiveData.observeAsState()
    if (lceState.value?.loading == true) Spinner()
    lceState.value?.error?.let { error ->
        Text("Error: $error")
    }
    lceState.value?.content?.let { equipment ->
        ConstraintLayout {
            val (img, text) = createRefs()

            AsyncImage(
                model = equipment.images?.first(),
                contentDescription = equipment.title ?: stringResource(R.string.placeholder_name),
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Text(text = equipment.title ?: stringResource(R.string.placeholder_name))
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun PreviewEquipmentDetailScreen() {
    WorldwideEquipmentGuideTheme {
        val lceLiveData = MutableLiveData(LCE(true))
        EquipmentDetailScreen(lceLiveData)
    }
}