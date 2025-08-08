package no.helge.android.featureone.ui

import javax.inject.Inject

class FeatureOneReducer @Inject constructor() : Reducer<FeatureOneUiState, FeatureOneAction, FeatureOneEffect> {
    override fun reduce(state: FeatureOneUiState, action: FeatureOneAction): Pair<FeatureOneUiState, FeatureOneEffect?> {
        return when (action) {
            FeatureOneAction.Increment -> state.copy(counter = state.counter + 1) to null
            FeatureOneAction.Decrement -> state.copy(counter = state.counter - 1) to null
            FeatureOneAction.NavigateToNext -> state to FeatureOneEffect.NavigateToFeatureTwo
            FeatureOneAction.LoadData -> state.copy(
                isLoading = true,
                errorMessage = null
            ) to null
            is FeatureOneAction.DataLoaded -> state.copy(
                isLoading = false,
                loadedData = action.result,
                errorMessage = null
            ) to null
            is FeatureOneAction.DataLoadFailed -> state.copy(
                isLoading = false,
                errorMessage = action.error
            ) to null
            FeatureOneAction.LoadInitialValue -> state.copy(
                isLoading = true,
                errorMessage = null
            ) to null
            is FeatureOneAction.InitialValueLoaded -> state.copy(
                counter = action.value,
                isLoading = false,
                errorMessage = null
            ) to null
            is FeatureOneAction.InitialValueLoadFailed -> state.copy(
                isLoading = false,
                errorMessage = action.error
            ) to null
        }
    }
}