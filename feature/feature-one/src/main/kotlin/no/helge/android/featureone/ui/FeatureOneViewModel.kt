package no.helge.android.featureone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import no.helge.android.featureone.domain.FeatureOneUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import no.helge.android.common.Result
import javax.inject.Inject

@HiltViewModel
class FeatureOneViewModel @Inject constructor(
    featureOneUseCase: FeatureOneUseCase
): ViewModel() {

    val uiState: StateFlow<FeatureOneUiState> = featureOneUseCase().map {
        when (it) {
            is Result.Error -> FeatureOneUiState.Error("Something went wrong")
            Result.Loading -> FeatureOneUiState.Loading
            is Result.Success -> FeatureOneUiState.Success(it.data)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FeatureOneUiState.Loading
    )
}