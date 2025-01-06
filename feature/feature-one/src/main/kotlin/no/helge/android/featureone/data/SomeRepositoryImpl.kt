package no.helge.android.featureone.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import no.helge.android.featureone.domain.SomeRepository
import javax.inject.Inject

class SomeRepositoryImpl @Inject constructor(): SomeRepository {

    override fun getData() = flow {
        println("Thread: ${Thread.currentThread().name}")
        delay(2000)
        emit("Feature One!")
    }.flowOn(Dispatchers.IO)
}