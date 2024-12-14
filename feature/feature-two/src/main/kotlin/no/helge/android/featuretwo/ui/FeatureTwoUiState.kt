package no.helge.android.featuretwo.ui

sealed interface FeatureTwoUiState {
    data object Loading : FeatureTwoUiState
    data class Success(val data: String) : FeatureTwoUiState
    data class Error(val message: String) : FeatureTwoUiState
}