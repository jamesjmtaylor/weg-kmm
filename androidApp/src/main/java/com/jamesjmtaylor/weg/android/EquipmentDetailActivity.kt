package com.jamesjmtaylor.weg.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.jamesjmtaylor.weg.android.subviews.ExpandableCard
import com.jamesjmtaylor.weg.android.subviews.ExpandableCardContent
import com.jamesjmtaylor.weg.android.subviews.ExpandableContent
import com.jamesjmtaylor.weg.android.subviews.Spinner
import com.jamesjmtaylor.weg.android.ui.theme.WorldwideEquipmentGuideTheme
import com.jamesjmtaylor.weg.models.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

//TODO: Add notes section
//TODO: TopBar is too complex and significantly slows down framerates.  Use an expandable card for each section
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
@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun EquipmentDetailScreen(vm: PreviewEquipmentDetailViewModel, modifier: Modifier = Modifier) {
    val lce by vm.lce.collectAsStateWithLifecycle()
    val expandedCardIds by vm.expandedCardIdsList.collectAsStateWithLifecycle()

    if (lce.loading) Spinner()
    if (lce.error != null) Text("Error: ${lce.error}")

    lce.content?.let { equipment ->
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Text(
                text = equipment.title ?: stringResource(R.string.placeholder_name),
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            val pagerState = rememberPagerState()
            HorizontalPager(count = equipment.images?.size ?: 0, state = pagerState) { index ->
                AsyncImage(
                    model = equipment.images?.get(index)?.url,
                    contentDescription = equipment.title ?: stringResource(R.string.placeholder_name),
                    placeholder = painterResource(id = R.drawable.placeholder),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                )
            }
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
            )
            val notes = equipment.details?.notes ?: stringResource(R.string.placeholder_notes)
            val country = "Country of Origin: ${equipment.details?.countryOfOrigin
                ?: stringResource(R.string.ina)}"
            val date = "Date of Introduction: ${equipment.details?.dateOfIntroduction?.description
                ?: stringResource(R.string.ina)}"
            val proliferation = "Proliferation: ${equipment.details?.proliferation ?: stringResource(R.string.ina)}"
            val cards = mutableListOf<ExpandableCardContent>()
            cards.add(ExpandableCardContent("Overview", "$notes\n$country\n$date\n$proliferation"))

            val variants = equipment.details?.variants
                ?.joinToString("\n") { "${it.name}: ${it.notes}" }
            if (!variants.isNullOrEmpty()) cards.add(ExpandableCardContent("Variants", variants))
            val properties : List<ExpandableCardContent?>? = equipment.details?.sections?.map { section ->
                if (!section.sections.isNullOrEmpty()) {
                    return@map null //TODO handle subsections, i.e. APFDS ammo, HEAT ammo, etc.
                } else if (!section.properties.isNullOrEmpty()) {
                    section.properties?.joinToString("\n") {
                        "${it.name}: ${it.value} ${it.units ?: ""}"
                    }?.let { content -> ExpandableCardContent(section.name, content) }
                } else {
                    return@map null
                }
            }
            properties?.let { properties.forEach { property -> property?.let { cards.add(it)}}}

            cards.forEachIndexed { index, content ->
                ExpandableCard(
                    content = content,
                    onCardClick = { vm.onCardArrowClicked(index) },
                    expanded = expandedCardIds.contains(index),
                )
            }
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
            dateOfIntroduction = DateOfIntroduction(1988, "1988"),
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