package hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.SomeRepositoryImpl
import domain.SomeRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSomeRepository(
        userRepositoryImpl: SomeRepositoryImpl
    ): SomeRepository
}