package no.helge.android.featureone.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeatureOneViewModel @Inject constructor(
    reducer: FeatureOneReducer,
    apiMiddleware: FeatureOneApiMiddleware
): BaseViewModel<FeatureOneUiState, FeatureOneAction, FeatureOneEffect>(
    reducer = reducer,
    middleware = listOf(apiMiddleware),
    initialState = FeatureOneUiState()
) {
    init {
        onAction(FeatureOneAction.LoadInitialValue)
    }
}