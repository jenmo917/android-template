package no.helge.android.featuretwo.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import no.helge.android.featuretwo.domain.FeatureTwoUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import no.helge.android.common.Result
import javax.inject.Inject

@HiltViewModel
class FeatureTwoViewModel @Inject constructor(
    featureTwoUseCase: FeatureTwoUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        val name = savedStateHandle.get<String>("name")
        val age = savedStateHandle.get<String>("age")
        println("name: $name, age: $age")
    }

    val uiState: StateFlow<FeatureTwoUiState> = featureTwoUseCase().map {
        when (it) {
            is Result.Error -> FeatureTwoUiState.Error("Something went wrong")
            Result.Loading -> FeatureTwoUiState.Loading
            is Result.Success -> FeatureTwoUiState.Success(it.data)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FeatureTwoUiState.Loading
    )
}