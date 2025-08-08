package no.helge.android.featureone.ui

import kotlinx.coroutines.delay
import no.helge.android.featureone.domain.SomeRepository
import javax.inject.Inject

class FeatureOneApiMiddleware @Inject constructor(
    private val repository: SomeRepository
) : Middleware<FeatureOneUiState, FeatureOneAction, FeatureOneEffect> {
    
    override suspend fun process(
        state: FeatureOneUiState,
        action: FeatureOneAction,
        dispatch: (FeatureOneAction) -> Unit
    ): FeatureOneAction? {
        return when (action) {
            FeatureOneAction.LoadData -> {
                try {
                    delay(1000)
                    val result = "Data loaded at ${System.currentTimeMillis()}"
                    dispatch(FeatureOneAction.DataLoaded(result))
                    null
                } catch (e: Exception) {
                    dispatch(FeatureOneAction.DataLoadFailed(e.message ?: "Unknown error"))
                    null
                }
            }
            FeatureOneAction.LoadInitialValue -> {
                try {
                    val initialValue = repository.getInitialCounterValue()
                    dispatch(FeatureOneAction.InitialValueLoaded(initialValue))
                    null
                } catch (e: Exception) {
                    dispatch(FeatureOneAction.InitialValueLoadFailed(e.message ?: "Failed to load initial value"))
                    null
                }
            }
            else -> action
        }
    }
}