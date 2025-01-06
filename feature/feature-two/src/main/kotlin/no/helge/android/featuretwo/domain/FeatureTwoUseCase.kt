package no.helge.android.featuretwo.domain

import kotlinx.coroutines.flow.Flow
import no.helge.android.common.Result
import no.helge.android.common.asResult
import javax.inject.Inject

class FeatureTwoUseCase @Inject constructor(
    private val someRepository: SomeRepository
) {

    operator fun invoke(): Flow<Result<String>> {
        return someRepository.getData()
            .asResult()

    }
}