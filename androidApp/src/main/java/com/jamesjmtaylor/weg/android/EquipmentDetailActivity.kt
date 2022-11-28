package com.jamesjmtaylor.weg.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jamesjmtaylor.weg.android.subviews.Spinner
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

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
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun EquipmentDetailScreen(vm: PreviewEquipmentDetailViewModel, modifier: Modifier = Modifier) {
    val lce by vm.lce.collectAsStateWithLifecycle()
    val expandedCardIds by vm.expandedCardIdsList.collectAsStateWithLifecycle()
    if (lce.loading) Spinner()
    if (lce.error != null) Text("Error: ${lce.error}")

    lce.content?.let { equipment ->
        Column {
            //TODO: Implement Image Pager per https://google.github.io/accompanist/pager/#usage
            Text(text = equipment.title ?: stringResource(R.string.placeholder_name))
            Spacer(Modifier.size(16.dp))
            AsyncImage(
                model = equipment.images?.first()?.url,
                contentDescription = equipment.title ?: stringResource(R.string.placeholder_name),
                placeholder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier.fillMaxWidth()
            )
            //TODO: Implement Details Views per https://proandroiddev.com/expandable-lists-in-jetpack-compose-b0b78c767b4

        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun PreviewEquipmentDetailScreen() {
    WorldwideEquipmentGuideTheme {
        val dimensions = listOf(Property("Length", "2m"), Property("Width", "1m"), Property("Height", ".5m"))
        val mainGun = listOf(Property("Caliber", "120mm"), Property("Ammo", "APFDS"))
        val sections = listOf(Section("Dimensions", dimensions), Section("Main Gun", mainGun))
        val variants = listOf(Variant("M1","The original."), Variant("M1A2", "The latest."))
        val details = SearchResultDetails(
            tiers = listOf(false,false,false,true),
            notes = "This is a tank",
            dateOfIntroduction = 1988,
            countryOfOrigin = "USA",
            proliferation = "USA",
            selectedRegions = emptyList(),
            checkedCountries = emptyList(),
            sections = sections,
            variants = variants,
            type = "WEG",
            version = 1
        )
        val images = listOf(Image("Abrams", "https://odin.tradoc.army.mil/mediawiki/images/4/44/M1A2%28C%29.jpg"))
        val searchResult = SearchResult("Tank", 0, listOf("Land"), images, details, 0)
        val lce = LCE(false, searchResult)
        val expandedIds = listOf(1)
        EquipmentDetailScreen(object : PreviewEquipmentDetailViewModel{
            override val lce: StateFlow<LCE>
                get() = object : StateFlow<LCE> {
                    override val replayCache = listOf(lce)
                    override val value = lce
                    override suspend fun collect(collector: FlowCollector<LCE>): Nothing {
                        throw Exception("collection exception")
                    }
                }
            override val expandedCardIdsList: StateFlow<List<Int>>
                get() = object : StateFlow<List<Int>> {
                    override val replayCache = listOf(expandedIds)
                    override val value = expandedIds
                    override suspend fun collect(collector: FlowCollector<List<Int>>): Nothing {
                        throw Exception("collection exception")
                    }
                }
            override fun getEquipmentDetails(id: Long) {}
            override fun onCardArrowClicked(cardId: Int) {}
        })
    }
}