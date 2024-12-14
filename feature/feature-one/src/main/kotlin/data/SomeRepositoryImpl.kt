package data

import domain.SomeRepository
import kotlinx.coroutines.delay
import no.helge.android.common.Either
import no.helge.android.common.Failure
import no.helge.android.common.toRight
import javax.inject.Inject

class SomeRepositoryImpl @Inject constructor(): SomeRepository {

    override suspend fun getData(): Either<Failure, String> {
        delay(2500)
        return "World".toRight()
    }
}