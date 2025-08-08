package no.helge.android.featureone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

interface Reducer<State, Action, Effect> {
    fun reduce(state: State, action: Action): Pair<State, Effect?>
}

interface Middleware<State, Action, Effect> {
    suspend fun process(
        state: State, 
        action: Action, 
        dispatch: (Action) -> Unit
    ): Action?
}

open class BaseViewModel<State, Action, Effect>(
    private val reducer: Reducer<State, Action, Effect>,
    private val middleware: List<Middleware<State, Action, Effect>> = emptyList(),
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    
    private val _effects = Channel<Effect>(Channel.UNLIMITED)
    val effects = _effects.receiveAsFlow()

    fun onAction(action: Action) {
        viewModelScope.launch {
            val processedAction = processMiddleware(action) ?: return@launch
            val (newState, effect) = reducer.reduce(_uiState.value, processedAction)
            _uiState.value = newState
            effect?.let { _effects.trySend(it) }
        }
    }
    
    private suspend fun processMiddleware(action: Action): Action? {
        return middleware.fold(action as Action?) { acc, middleware ->
            acc?.let { middleware.process(_uiState.value, it, ::onAction) }
        }
    }
}