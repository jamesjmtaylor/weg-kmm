package com.jamesjmtaylor.weg2015.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val vm: EquipmentViewModel by viewModels { EquipmentViewModel.Factory }

    /**
     * See [consume UI state](https://developer.android.com/topic/architecture/ui-layer#consume-ui-state)
     * for more information.
     * TODO: https://developer.android.com/codelabs/android-paging#0
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { updateUI(it) }
            }
        }
        vm.fetchEquipment()
    }

    private fun updateUI(state: EquipmentUiState) {
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EquipmentScreen(state)
                }
            }
        }
    }
}


@Composable
fun EquipmentScreen(equipmentUiState: EquipmentUiState) {
    if (equipmentUiState.loading) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
    equipmentUiState.equipment?.let { Text(text = it)}
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        val state = EquipmentUiState()
        EquipmentScreen(state)
    }
}
