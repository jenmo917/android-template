package no.helge.android.featuretwo.data

import kotlinx.coroutines.delay
import no.helge.android.featuretwo.domain.SomeRepository
import no.helge.android.common.Either
import no.helge.android.common.Failure
import no.helge.android.common.toRight
import javax.inject.Inject

class SomeRepositoryImpl @Inject constructor(): SomeRepository {

    override suspend fun getData(): Either<Failure, String> {
        delay(2000)
        return "Feature Two".toRight()
    }
}