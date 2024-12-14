package no.helge.android.featuretwo.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.helge.android.featuretwo.data.SomeRepositoryImpl
import no.helge.android.featuretwo.domain.SomeRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSomeRepository(
        userRepositoryImpl: SomeRepositoryImpl
    ): SomeRepository
}