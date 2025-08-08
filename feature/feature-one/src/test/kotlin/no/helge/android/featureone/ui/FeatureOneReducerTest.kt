package no.helge.android.featureone.ui

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class FeatureOneReducerTest {

    private lateinit var reducer: FeatureOneReducer

    @Before
    fun setUp() {
        reducer = FeatureOneReducer()
    }

    @Test
    fun `reduce with Increment action should increment counter`() {
        // Given
        val initialState = FeatureOneUiState(counter = 5, isLoading = false)
        val action = FeatureOneAction.Increment

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(6)
        assertThat(newState.isLoading).isFalse()
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with Decrement action should decrement counter`() {
        // Given
        val initialState = FeatureOneUiState(counter = 10, isLoading = false)
        val action = FeatureOneAction.Decrement

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(9)
        assertThat(newState.isLoading).isFalse()
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with Decrement from zero should go to negative`() {
        // Given
        val initialState = FeatureOneUiState(counter = 0, isLoading = false)
        val action = FeatureOneAction.Decrement

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(-1)
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with NavigateToNext should produce NavigateToFeatureTwo effect`() {
        // Given
        val initialState = FeatureOneUiState(counter = 5)
        val action = FeatureOneAction.NavigateToNext

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState).isEqualTo(initialState) // State should remain unchanged
        assertThat(effect).isEqualTo(FeatureOneEffect.NavigateToFeatureTwo)
    }

    @Test
    fun `reduce with LoadData should set loading state`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 3,
            isLoading = false,
            errorMessage = "Previous error"
        )
        val action = FeatureOneAction.LoadData

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(3) // Counter unchanged
        assertThat(newState.isLoading).isTrue()
        assertThat(newState.errorMessage).isNull() // Error cleared
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with DataLoaded should clear loading and set data`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 5,
            isLoading = true,
            errorMessage = "Some error"
        )
        val loadedData = "Successfully loaded data"
        val action = FeatureOneAction.DataLoaded(loadedData)

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(5) // Counter unchanged
        assertThat(newState.isLoading).isFalse()
        assertThat(newState.loadedData).isEqualTo(loadedData)
        assertThat(newState.errorMessage).isNull() // Error cleared
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with DataLoadFailed should clear loading and set error`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 7,
            isLoading = true,
            loadedData = "Previous data"
        )
        val errorMessage = "Failed to load data"
        val action = FeatureOneAction.DataLoadFailed(errorMessage)

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(7) // Counter unchanged
        assertThat(newState.isLoading).isFalse()
        assertThat(newState.loadedData).isEqualTo("Previous data") // Previous data preserved
        assertThat(newState.errorMessage).isEqualTo(errorMessage)
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with LoadInitialValue should set loading state`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 0,
            isLoading = false,
            errorMessage = "Old error"
        )
        val action = FeatureOneAction.LoadInitialValue

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(0) // Counter unchanged
        assertThat(newState.isLoading).isTrue()
        assertThat(newState.errorMessage).isNull() // Error cleared
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with InitialValueLoaded should update counter and clear loading`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 0,
            isLoading = true,
            errorMessage = "Some error"
        )
        val initialValue = 42
        val action = FeatureOneAction.InitialValueLoaded(initialValue)

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(initialValue)
        assertThat(newState.isLoading).isFalse()
        assertThat(newState.errorMessage).isNull() // Error cleared
        assertThat(effect).isNull()
    }

    @Test
    fun `reduce with InitialValueLoadFailed should clear loading and set error`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 0,
            isLoading = true
        )
        val errorMessage = "Failed to load initial value"
        val action = FeatureOneAction.InitialValueLoadFailed(errorMessage)

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(0) // Counter unchanged
        assertThat(newState.isLoading).isFalse()
        assertThat(newState.errorMessage).isEqualTo(errorMessage)
        assertThat(effect).isNull()
    }

    @Test
    fun `reducer should preserve unrelated state properties during updates`() {
        // Given
        val initialState = FeatureOneUiState(
            counter = 15,
            isLoading = false,
            loadedData = "Important data",
            errorMessage = null
        )
        val action = FeatureOneAction.Increment

        // When
        val (newState, effect) = reducer.reduce(initialState, action)

        // Then
        assertThat(newState.counter).isEqualTo(16) // Only counter should change
        assertThat(newState.isLoading).isEqualTo(initialState.isLoading)
        assertThat(newState.loadedData).isEqualTo(initialState.loadedData)
        assertThat(newState.errorMessage).isEqualTo(initialState.errorMessage)
        assertThat(effect).isNull()
    }
}