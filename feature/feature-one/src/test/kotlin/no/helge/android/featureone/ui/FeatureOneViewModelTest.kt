package no.helge.android.featureone.ui

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import no.helge.android.featureone.domain.SomeRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class FeatureOneViewModelTest {

    @Mock
    private lateinit var mockRepository: SomeRepository

    private lateinit var viewModel: FeatureOneViewModel
    private lateinit var reducer: FeatureOneReducer
    private lateinit var middleware: FeatureOneApiMiddleware

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        reducer = FeatureOneReducer()
        middleware = FeatureOneApiMiddleware(mockRepository)
    }

    private fun createViewModel(): FeatureOneViewModel {
        return FeatureOneViewModel(reducer, middleware)
    }

    @Test
    fun `viewModel initial state should be loading with counter 0`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(42)
        
        // When
        viewModel = createViewModel()

        // Then
        val initialState = viewModel.uiState.value
        assertThat(initialState.counter).isEqualTo(0)
        assertThat(initialState.isLoading).isTrue()
        assertThat(initialState.loadedData).isNull()
        assertThat(initialState.errorMessage).isNull()
    }

    @Test
    fun `viewModel should load initial counter value on init`() = runTest {
        // Given
        val expectedInitialValue = 25
        whenever(mockRepository.getInitialCounterValue()).thenReturn(expectedInitialValue)

        // When
        viewModel = createViewModel()

        // Wait for the middleware to process the initial load
        Thread.sleep(100) // Give time for async operation

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(expectedInitialValue)
        assertThat(finalState.isLoading).isFalse()
        assertThat(finalState.errorMessage).isNull()
    }

    @Test
    fun `viewModel should handle initial load failure gracefully`() = runTest {
        // Given
        val errorMessage = "Network error"
        whenever(mockRepository.getInitialCounterValue()).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel = createViewModel()

        // Wait for the middleware to process the initial load
        Thread.sleep(100)

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(0) // Should remain at default
        assertThat(finalState.isLoading).isFalse()
        assertThat(finalState.errorMessage).isEqualTo(errorMessage)
    }

    @Test
    fun `onAction Increment should increase counter by 1`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(10)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        val initialCounter = viewModel.uiState.value.counter

        // When
        viewModel.onAction(FeatureOneAction.Increment)

        // Then
        val newState = viewModel.uiState.value
        assertThat(newState.counter).isEqualTo(initialCounter + 1)
        assertThat(newState.isLoading).isFalse()
    }

    @Test
    fun `onAction Decrement should decrease counter by 1`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(10)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        val initialCounter = viewModel.uiState.value.counter

        // When
        viewModel.onAction(FeatureOneAction.Decrement)

        // Then
        val newState = viewModel.uiState.value
        assertThat(newState.counter).isEqualTo(initialCounter - 1)
        assertThat(newState.isLoading).isFalse()
    }

    @Test
    fun `multiple increment actions should compound correctly`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(5)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        // When
        repeat(3) {
            viewModel.onAction(FeatureOneAction.Increment)
        }

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(8) // 5 + 3
    }

    @Test
    fun `mixed increment and decrement actions should calculate correctly`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(10)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        // When
        viewModel.onAction(FeatureOneAction.Increment) // 10 -> 11
        viewModel.onAction(FeatureOneAction.Increment) // 11 -> 12
        viewModel.onAction(FeatureOneAction.Decrement) // 12 -> 11
        viewModel.onAction(FeatureOneAction.Increment) // 11 -> 12

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(12)
    }

    @Test
    fun `onAction NavigateToNext should emit NavigateToFeatureTwo effect`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(1)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        // When
        viewModel.onAction(FeatureOneAction.NavigateToNext)

        // Then
        val effect = viewModel.effects.first()
        assertThat(effect).isEqualTo(FeatureOneEffect.NavigateToFeatureTwo)
    }

    @Test
    fun `counter operations should not affect loading or error state after initial load`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(0)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        // When
        viewModel.onAction(FeatureOneAction.Increment)
        viewModel.onAction(FeatureOneAction.Decrement)

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(0) // 0 + 1 - 1 = 0
        assertThat(finalState.isLoading).isFalse()
        assertThat(finalState.errorMessage).isNull()
    }

    @Test
    fun `viewModel should preserve other state properties during counter operations`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(5)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        val stateAfterLoad = viewModel.uiState.value

        // When
        viewModel.onAction(FeatureOneAction.Increment)

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(6) // Only counter should change
        assertThat(finalState.isLoading).isEqualTo(stateAfterLoad.isLoading)
        assertThat(finalState.loadedData).isEqualTo(stateAfterLoad.loadedData)
        assertThat(finalState.errorMessage).isEqualTo(stateAfterLoad.errorMessage)
    }

    @Test
    fun `viewModel should handle negative counter values`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(0)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        // When
        repeat(3) {
            viewModel.onAction(FeatureOneAction.Decrement)
        }

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(-3)
    }

    @Test
    fun `viewModel should handle large counter values`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(999999)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        // When
        viewModel.onAction(FeatureOneAction.Increment)

        // Then
        val finalState = viewModel.uiState.value
        assertThat(finalState.counter).isEqualTo(1000000)
    }

    @Test
    fun `navigation effect should not interfere with counter state`() = runTest {
        // Given
        whenever(mockRepository.getInitialCounterValue()).thenReturn(42)
        viewModel = FeatureOneViewModel(reducer, middleware)
        Thread.sleep(100) // Wait for initial load

        val stateBeforeNavigation = viewModel.uiState.value

        // When
        viewModel.onAction(FeatureOneAction.NavigateToNext)

        // Then
        val stateAfterNavigation = viewModel.uiState.value
        assertThat(stateAfterNavigation).isEqualTo(stateBeforeNavigation)
        
        // And navigation effect should still be emitted
        val effect = viewModel.effects.first()
        assertThat(effect).isEqualTo(FeatureOneEffect.NavigateToFeatureTwo)
    }
}