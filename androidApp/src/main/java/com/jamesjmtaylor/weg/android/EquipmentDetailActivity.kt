package com.jamesjmtaylor.weg.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jamesjmtaylor.weg.android.subviews.Spinner
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme

class EquipmentDetailActivity : ComponentActivity()  {
    private val vm : EquipmentDetailViewModel by viewModels { EquipmentDetailViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id : Long = intent.extras?.getLong(EQUIPMENT_ID_KEY) ?: return
        vm.getEquipmentDetails(id)
        setContent { EquipmentDetailScreen(vm) }
    }
}

const val EQUIPMENT_ID_KEY = "EQUIPMENT_ID_KEY"
//TODO: Finish https://proandroiddev.com/expandable-lists-in-jetpack-compose-b0b78c767b4 for expandable list.
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun EquipmentDetailScreen(vm: PreviewEquipmentDetailViewModel, modifier: Modifier = Modifier) {
    val lce by vm.lce.collectAsStateWithLifecycle()
    val expandedCardIds by vm.expandedCardIdsList.collectAsStateWithLifecycle()
    if (lce.loading) Spinner()
    if (lce.error != null) Text("Error: ${lce.error}")

    lce.content?.let { equipment ->
        ConstraintLayout {
            val (img, text) = createRefs()
            //TODO: Implement Image Pager per https://google.github.io/accompanist/pager/#usage
            AsyncImage(
                model = equipment.images?.first(),
                contentDescription = equipment.title ?: stringResource(R.string.placeholder_name),
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            //TODO: Implement Details Views.
            Text(text = equipment.title ?: stringResource(R.string.placeholder_name))
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun PreviewEquipmentDetailScreen() {
    WorldwideEquipmentGuideTheme {
        val vm = EquipmentDetailViewModel()
        EquipmentDetailScreen(lceLiveData)
    }
}