package no.helge.android.featureone.ui

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import no.helge.android.featureone.domain.SomeRepository
import no.helge.android.featureone.hilt.RepositoryModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestFeatureOneModule {
    
    @Binds
    @Singleton
    abstract fun bindRepository(
        fakeRepository: FakeFeatureOneRepository
    ): SomeRepository
}