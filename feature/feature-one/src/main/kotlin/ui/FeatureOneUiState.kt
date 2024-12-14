package ui

sealed interface FeatureOneUiState {
    data object Loading : FeatureOneUiState
    data class Success(val data: String) : FeatureOneUiState
    data class Error(val message: String) : FeatureOneUiState
}