package no.helge.android.featureone.ui

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.helge.android.featureone.domain.SomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeFeatureOneRepository @Inject constructor() : SomeRepository {
    
    private var initialCounterValue = 1
    private var shouldFailInitialLoad = false
    private var initialValueDelayMs = 0L
    
    fun setInitialCounterValue(value: Int) {
        initialCounterValue = value
    }
    
    fun setShouldFailInitialLoad(shouldFail: Boolean) {
        shouldFailInitialLoad = shouldFail
    }
    
    fun setInitialValueDelay(delayMs: Long) {
        initialValueDelayMs = delayMs
    }
    
    override fun getData(): Flow<String> = flow {
        emit("Test data")
    }
    
    override suspend fun getInitialCounterValue(): Int {
        if (initialValueDelayMs > 0) {
            delay(initialValueDelayMs)
        }
        
        if (shouldFailInitialLoad) {
            throw RuntimeException("Failed to load initial counter value")
        }
        
        return initialCounterValue
    }
}