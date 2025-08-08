package no.helge.android.featuretwo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeatureTwoViewModel @Inject constructor(
    private val stateManager: FeatureTwoStateManager
): ViewModel() {

    val uiState: StateFlow<FeatureTwoUiState> = stateManager.uiState
    val name: String? = stateManager.name
    val age: String? = stateManager.age

    fun onAction(action: FeatureTwoAction) {
        stateManager.handleAction(action, viewModelScope)
    }

    init {
        println("name: $name, age: $age")
        onAction(FeatureTwoAction.LoadData)
    }
}