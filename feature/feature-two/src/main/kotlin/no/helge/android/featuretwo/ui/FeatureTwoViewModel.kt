package no.helge.android.featuretwo.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import no.helge.android.featuretwo.domain.SomeUseCase
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
    private val someUseCase: SomeUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        val name = savedStateHandle.get<String>("name")
        val age = savedStateHandle.get<String>("age")
        println("name: $name, age: $age")
    }

    private val _state: MutableStateFlow<FeatureTwoUiState> = MutableStateFlow(FeatureTwoUiState.Loading)
    val state: StateFlow<FeatureTwoUiState> = _state

    fun handleIntent(intent: FeatureOneIntent) {
        viewModelScope.launch {
            when (intent) {
                is FeatureOneIntent.LoadData -> loadData()
            }
        }
    }

    private suspend fun loadData() {
        _state.value = FeatureTwoUiState.Loading
        someUseCase.run(UseCase.None()).onSuccess {
            _state.value = FeatureTwoUiState.Success(it)
        }.onFailure {
            _state.value = FeatureTwoUiState.Error("Something went wrong")
        }
    }
}