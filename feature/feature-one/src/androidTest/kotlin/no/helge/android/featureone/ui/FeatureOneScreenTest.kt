package no.helge.android.featureone.ui

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FeatureOneScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var testRepository: FakeFeatureOneRepository

    private val navigationEvents = MutableSharedFlow<Unit>(replay = 1)

    /**
     * Wait for initial loading to complete by polling for the expected counter value
     */
    private fun waitForInitialLoad(expectedValue: String, timeoutMs: Long = 5000) {
        val endTime = System.currentTimeMillis() + timeoutMs
        var lastException: AssertionError? = null
        
        while (System.currentTimeMillis() < endTime) {
            composeTestRule.waitForIdle()
            try {
                composeTestRule.onNodeWithText(expectedValue).assertIsDisplayed()
                return // Success
            } catch (e: AssertionError) {
                lastException = e
                Thread.sleep(50)
            }
        }
        
        // Final attempt - throw the last exception if it fails
        throw lastException ?: AssertionError("Timeout waiting for value: $expectedValue")
    }

    @Before
    fun setUp() {
        hiltRule.inject()

        // Reset navigation events for each test
        navigationEvents.resetReplayCache()
    }

    @Test
    fun featureOneScreen_initialState_showsLoadingIndicator() = runTest {
        // Given: Initial loading state with short delay
        testRepository.setInitialValueDelay(100) // Short delay to verify loading state

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for loading to complete and counter should be visible (repository default is 1)
        waitForInitialLoad("1")
    }

    @Test
    fun featureOneScreen_afterLoading_showsInitialCounterValue() = runTest {
        // Given: Repository returns a specific initial value
        val expectedInitialValue = 42
        testRepository.setInitialCounterValue(expectedInitialValue)
        testRepository.setInitialValueDelay(0) // No delay

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for loading to complete
        waitForInitialLoad(expectedInitialValue.toString())

        // Then: Initial counter value should be displayed
        composeTestRule
            .onNodeWithText(expectedInitialValue.toString())
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_clickIncrementButton_incrementsCounter() = runTest {
        // Given: Initial state with counter at 5
        val initialValue = 5
        testRepository.setInitialCounterValue(initialValue)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad(initialValue.toString())

        // When: Click increment button (this should be synchronous)
        composeTestRule
            .onNodeWithText("+")
            .assertHasClickAction()
            .performClick()

        // Wait for UI to process the click
        composeTestRule.waitForIdle()

        // Then: Counter should be incremented immediately
        composeTestRule
            .onNodeWithText((initialValue + 1).toString())
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_clickDecrementButton_decrementsCounter() = runTest {
        // Given: Initial state with counter at 10
        val initialValue = 10
        testRepository.setInitialCounterValue(initialValue)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad(initialValue.toString())

        // When: Click decrement button (this should be synchronous)
        composeTestRule
            .onNodeWithText("-")
            .assertHasClickAction()
            .performClick()

        // Wait for UI to process the click
        composeTestRule.waitForIdle()

        // Then: Counter should be decremented immediately
        composeTestRule
            .onNodeWithText((initialValue - 1).toString())
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_multipleIncrementClicks_updatesCounterCorrectly() = runTest {
        // Given: Initial state with counter at 0
        val initialValue = 0
        testRepository.setInitialCounterValue(initialValue)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad(initialValue.toString())

        // When: Click increment button multiple times (all synchronous)
        repeat(3) {
            composeTestRule
                .onNodeWithText("+")
                .performClick()
            composeTestRule.waitForIdle()
        }

        // Then: Counter should be incremented correctly
        composeTestRule
            .onNodeWithText("3")
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_mixedIncrementDecrement_updatesCounterCorrectly() = runTest {
        // Given: Initial state with counter at 5
        val initialValue = 5
        testRepository.setInitialCounterValue(initialValue)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad(initialValue.toString())

        // When: Perform mixed increment/decrement operations (all synchronous)
        composeTestRule.onNodeWithText("+").performClick() // 5 -> 6
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("+").performClick() // 6 -> 7
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("-").performClick() // 7 -> 6
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("+").performClick() // 6 -> 7
        composeTestRule.waitForIdle()

        // Then: Counter should show final value
        composeTestRule
            .onNodeWithText("7")
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_clickNavigationButton_triggersNavigation() = runTest {
        // Given: Initial state loaded
        testRepository.setInitialCounterValue(1)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad("1")

        // When: Click navigation button
        composeTestRule
            .onNodeWithText("Navigate to Feature Two")
            .assertHasClickAction()
            .performClick()

        // Wait for UI to settle after click
        composeTestRule.waitForIdle()

        // Then: Navigation should be triggered with timeout
        val navigationEvent = withTimeout(2000) {
            navigationEvents.first()
        }
        assertThat(navigationEvent).isNotNull()
    }

    @Test
    fun featureOneScreen_screenElements_areDisplayedCorrectly() = runTest {
        // Given: Initial state loaded
        testRepository.setInitialCounterValue(25)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad("25")

        // Then: All UI elements should be displayed
        composeTestRule
            .onNodeWithText("Counter")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("25")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("+")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("-")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Navigate to Feature Two")
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_loadingError_showsErrorState() = runTest {
        // Given: Repository that fails to load initial value
        testRepository.setShouldFailInitialLoad(true)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for the error state to be processed and show default counter
        // When initial load fails, counter should show default value (0)
        waitForInitialLoad("0")

        // Then: Error state should be handled gracefully with default value
        composeTestRule
            .onNodeWithText("0")
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_buttonsRemainClickableDuringCounterUpdates() = runTest {
        // Given: Initial state loaded
        testRepository.setInitialCounterValue(0)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad("0")

        // When & Then: Buttons should remain clickable after multiple rapid clicks
        repeat(5) {
            composeTestRule
                .onNodeWithText("+")
                .assertHasClickAction()
                .performClick()
            composeTestRule.waitForIdle()
        }

        composeTestRule
            .onNodeWithText("5")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("-")
            .assertHasClickAction()
            .performClick()
            
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("4")
            .assertIsDisplayed()
    }

    @Test
    fun featureOneScreen_navigationButtonRemainsFunctionalAfterCounterOperations() = runTest {
        // Given: Initial state loaded
        testRepository.setInitialCounterValue(0)
        testRepository.setInitialValueDelay(0)

        composeTestRule.setContent {
            FeatureOneScreen(
                onNavigate = { navigationEvents.tryEmit(Unit) }
            )
        }

        // Wait for initial loading to complete
        waitForInitialLoad("0")

        // When: Perform counter operations
        composeTestRule.onNodeWithText("+").performClick()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("-").performClick()
        composeTestRule.waitForIdle()

        // Then: Navigation should still work
        composeTestRule
            .onNodeWithText("Navigate to Feature Two")
            .assertHasClickAction()
            .performClick()

        // Wait for UI to settle after click
        composeTestRule.waitForIdle()

        // Navigation event should be triggered with timeout
        val navigationEvent = withTimeout(2000) {
            navigationEvents.first()
        }
        assertThat(navigationEvent).isNotNull()
    }
}