package no.helge.android.featureone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import no.helge.core.designsystem.component.CustomButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                FeatureOneEffect.NavigateToFeatureTwo -> onNavigate()
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Counter",
                fontSize = 24.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CustomButton(onClick = {
                    viewModel.onAction(FeatureOneAction.Decrement)
                }) {
                    Text(text = "-", fontSize = 24.sp)
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(96.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text(
                            text = "${state.counter}",
                            fontSize = 48.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(32.dp))
                
                CustomButton(onClick = {
                    viewModel.onAction(FeatureOneAction.Increment)
                }) {
                    Text(text = "+", fontSize = 24.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CustomButton(onClick = {
                viewModel.onAction(FeatureOneAction.NavigateToNext)
            }) {
                Text(text = "Navigate to Feature Two")
            }
        }
    }
}

@Preview
@Composable
private fun FeatureOneScreenPreview() {
    FeatureOneScreen()
}