package no.helge.android.featuretwo.domain

import no.helge.android.common.Either
import no.helge.android.common.Failure

interface SomeRepository {
    suspend fun getData(): Either<Failure, String>
}