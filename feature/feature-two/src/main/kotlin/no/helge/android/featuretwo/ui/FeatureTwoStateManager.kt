package no.helge.android.featuretwo.ui

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.helge.android.featuretwo.domain.FeatureTwoUseCase
import no.helge.android.common.Result
import javax.inject.Inject

class FeatureTwoStateManager @Inject constructor(
    private val featureTwoUseCase: FeatureTwoUseCase,
    private val savedStateHandle: SavedStateHandle
) {
    private val _uiState = MutableStateFlow<FeatureTwoUiState>(FeatureTwoUiState.Loading)
    val uiState: StateFlow<FeatureTwoUiState> = _uiState.asStateFlow()

    val name: String? = savedStateHandle.get<String>("name")
    val age: String? = savedStateHandle.get<String>("age")

    fun reduce(state: FeatureTwoUiState, action: FeatureTwoAction): FeatureTwoUiState {
        return when (action) {
            FeatureTwoAction.LoadData -> FeatureTwoUiState.Loading
            FeatureTwoAction.Retry -> FeatureTwoUiState.Loading
            FeatureTwoAction.NavigateBack -> state // No state change for navigation
        }
    }

    fun handleAction(action: FeatureTwoAction, scope: CoroutineScope) {
        val newState = reduce(_uiState.value, action)
        _uiState.value = newState

        when (action) {
            FeatureTwoAction.LoadData, FeatureTwoAction.Retry -> {
                scope.launch {
                    featureTwoUseCase().collect { result ->
                        _uiState.value = mapResultToState(result)
                    }
                }
            }
            FeatureTwoAction.NavigateBack -> {
                // Navigation logic would be handled by the ViewModel or UI layer
            }
        }
    }

    private fun mapResultToState(result: Result<String>): FeatureTwoUiState {
        return when (result) {
            is Result.Error -> FeatureTwoUiState.Error("Something went wrong")
            Result.Loading -> FeatureTwoUiState.Loading
            is Result.Success -> FeatureTwoUiState.Success(result.data)
        }
    }
}