package no.helge.android.featureone.ui

sealed class FeatureOneAction {
    data object Increment : FeatureOneAction()
    data object Decrement : FeatureOneAction()
    data object NavigateToNext : FeatureOneAction()
    data object LoadData : FeatureOneAction()
    data class DataLoaded(val result: String) : FeatureOneAction()
    data class DataLoadFailed(val error: String) : FeatureOneAction()
    data object LoadInitialValue : FeatureOneAction()
    data class InitialValueLoaded(val value: Int) : FeatureOneAction()
    data class InitialValueLoadFailed(val error: String) : FeatureOneAction()
}