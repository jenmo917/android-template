package no.helge.android.featureone.ui

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import no.helge.android.featureone.domain.SomeRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class FeatureOneApiMiddlewareTest {

    @Mock
    private lateinit var mockRepository: SomeRepository

    private lateinit var middleware: FeatureOneApiMiddleware
    
    private val dispatchedActions = mutableListOf<FeatureOneAction>()
    
    private fun testDispatch(action: FeatureOneAction) {
        dispatchedActions.add(action)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        middleware = FeatureOneApiMiddleware(mockRepository)
        dispatchedActions.clear()
    }

    @Test
    fun `process LoadData action should dispatch DataLoaded on success`() = runTest {
        // Given
        val initialState = FeatureOneUiState(counter = 5)
        val action = FeatureOneAction.LoadData

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isNull() // LoadData should consume the action
        assertThat(dispatchedActions).hasSize(1)
        
        val dispatchedAction = dispatchedActions.first()
        assertThat(dispatchedAction).isInstanceOf(FeatureOneAction.DataLoaded::class.java)
        
        val dataLoadedAction = dispatchedAction as FeatureOneAction.DataLoaded
        assertThat(dataLoadedAction.result).startsWith("Data loaded at")
    }

    @Test
    fun `process LoadInitialValue action should dispatch InitialValueLoaded on success`() = runTest {
        // Given
        val expectedValue = 42
        whenever(mockRepository.getInitialCounterValue()).thenReturn(expectedValue)
        
        val initialState = FeatureOneUiState()
        val action = FeatureOneAction.LoadInitialValue

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isNull() // Action should be consumed
        assertThat(dispatchedActions).hasSize(1)
        
        val dispatchedAction = dispatchedActions.first()
        assertThat(dispatchedAction).isInstanceOf(FeatureOneAction.InitialValueLoaded::class.java)
        
        val initialValueLoadedAction = dispatchedAction as FeatureOneAction.InitialValueLoaded
        assertThat(initialValueLoadedAction.value).isEqualTo(expectedValue)
    }

    @Test
    fun `process LoadInitialValue action should dispatch InitialValueLoadFailed on error`() = runTest {
        // Given
        val errorMessage = "Repository error"
        whenever(mockRepository.getInitialCounterValue()).thenThrow(RuntimeException(errorMessage))
        
        val initialState = FeatureOneUiState()
        val action = FeatureOneAction.LoadInitialValue

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isNull() // Action should be consumed
        assertThat(dispatchedActions).hasSize(1)
        
        val dispatchedAction = dispatchedActions.first()
        assertThat(dispatchedAction).isInstanceOf(FeatureOneAction.InitialValueLoadFailed::class.java)
        
        val failedAction = dispatchedAction as FeatureOneAction.InitialValueLoadFailed
        assertThat(failedAction.error).isEqualTo(errorMessage)
    }

    @Test
    fun `process LoadInitialValue action should handle exception with null message`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenThrow(RuntimeException(null as String?))
        
        val initialState = FeatureOneUiState()
        val action = FeatureOneAction.LoadInitialValue

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isNull()
        assertThat(dispatchedActions).hasSize(1)
        
        val dispatchedAction = dispatchedActions.first()
        assertThat(dispatchedAction).isInstanceOf(FeatureOneAction.InitialValueLoadFailed::class.java)
        
        val failedAction = dispatchedAction as FeatureOneAction.InitialValueLoadFailed
        assertThat(failedAction.error).isEqualTo("Failed to load initial value")
    }

    @Test
    fun `process non-handled actions should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState(counter = 10)
        val action = FeatureOneAction.Increment

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action) // Action should pass through
        assertThat(dispatchedActions).isEmpty() // No actions should be dispatched
    }

    @Test
    fun `process Decrement action should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState(counter = 5)
        val action = FeatureOneAction.Decrement

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action)
        assertThat(dispatchedActions).isEmpty()
    }

    @Test
    fun `process NavigateToNext action should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState(counter = 5)
        val action = FeatureOneAction.NavigateToNext

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action)
        assertThat(dispatchedActions).isEmpty()
    }

    @Test
    fun `process DataLoaded action should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState(counter = 5)
        val action = FeatureOneAction.DataLoaded("test data")

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action)
        assertThat(dispatchedActions).isEmpty()
    }

    @Test
    fun `process DataLoadFailed action should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState(counter = 5)
        val action = FeatureOneAction.DataLoadFailed("error")

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action)
        assertThat(dispatchedActions).isEmpty()
    }

    @Test
    fun `process InitialValueLoaded action should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState()
        val action = FeatureOneAction.InitialValueLoaded(25)

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action)
        assertThat(dispatchedActions).isEmpty()
    }

    @Test
    fun `process InitialValueLoadFailed action should pass through unchanged`() = runTest {
        // Given
        val initialState = FeatureOneUiState()
        val action = FeatureOneAction.InitialValueLoadFailed("error")

        // When
        val result = middleware.process(initialState, action, ::testDispatch)

        // Then
        assertThat(result).isEqualTo(action)
        assertThat(dispatchedActions).isEmpty()
    }
}