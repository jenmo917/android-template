package no.helge.android.featureone.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FeatureOneScreen(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit = {},
    viewModel: FeatureOneViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState = state

    @Composable
    fun BoxWithMessage(message: String) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = message, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onNavigate) {
                    Text(text = "Navigate to Feature two")
                }
            }
        }
    }

    when (currentState) {
        FeatureOneUiState.Loading -> BoxWithMessage("Loading...")
        is FeatureOneUiState.Success -> BoxWithMessage("Hello ${currentState.data}!")
        is FeatureOneUiState.Error -> BoxWithMessage("Error: ${currentState.message}!")
    }
}

@Preview
@Composable
private fun FeatureOneScreenPreview() {
    FeatureOneScreen()
}