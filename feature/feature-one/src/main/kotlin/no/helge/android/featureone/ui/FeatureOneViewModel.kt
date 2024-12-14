package no.helge.android.featureone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import no.helge.android.featureone.domain.SomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.helge.android.common.onFailure
import no.helge.android.common.onSuccess
import no.helge.android.core.domain.UseCase
import javax.inject.Inject

sealed class FeatureOneIntent {
    data object LoadData : FeatureOneIntent()
}

@HiltViewModel
class FeatureOneViewModel @Inject constructor(
    private val someUseCase: SomeUseCase
): ViewModel() {

    private val _state: MutableStateFlow<FeatureOneUiState> = MutableStateFlow(FeatureOneUiState.Loading)
    val state: StateFlow<FeatureOneUiState> = _state

    fun handleIntent(intent: FeatureOneIntent) {
        viewModelScope.launch {
            when (intent) {
                is FeatureOneIntent.LoadData -> loadData()
            }
        }
    }

    private suspend fun loadData() {
        _state.value = FeatureOneUiState.Loading
        someUseCase.run(UseCase.None()).onSuccess {
            _state.value = FeatureOneUiState.Success(it)
        }.onFailure {
            _state.value = FeatureOneUiState.Error("Something went wrong")
        }
    }
}