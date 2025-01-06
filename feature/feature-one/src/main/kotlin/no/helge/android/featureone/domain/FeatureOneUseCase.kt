package no.helge.android.featureone.domain

import kotlinx.coroutines.flow.Flow
import no.helge.android.common.Result
import no.helge.android.common.asResult
import javax.inject.Inject

class FeatureOneUseCase @Inject constructor(
    private val someRepository: SomeRepository
) {

    operator fun invoke(): Flow<Result<String>> {
        return someRepository.getData()
            .asResult()
    }
}