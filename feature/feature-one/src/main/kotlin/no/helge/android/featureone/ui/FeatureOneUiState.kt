package no.helge.android.featureone.ui

data class FeatureOneUiState(
    val counter: Int = 0,
    val isLoading: Boolean = true,
    val loadedData: String? = null,
    val errorMessage: String? = null
)