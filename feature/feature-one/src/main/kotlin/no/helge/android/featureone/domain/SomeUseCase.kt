package no.helge.android.featureone.domain

import no.helge.android.common.Either
import no.helge.android.common.Failure
import no.helge.android.core.domain.UseCase
import javax.inject.Inject

class SomeUseCase @Inject constructor(
    private val someRepository: SomeRepository
): UseCase<String, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, String> {
        return someRepository.getData()
    }
}