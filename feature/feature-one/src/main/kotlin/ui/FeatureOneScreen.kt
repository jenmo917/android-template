package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FeatureOneScreen(
    modifier: Modifier = Modifier,
    viewModel: FeatureOneViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.handleIntent(FeatureOneIntent.LoadData)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentState = state

    @Composable
    fun BoxWithMessage(message: String) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = message, fontSize = 24.sp)
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