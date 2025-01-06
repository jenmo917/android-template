package no.helge.android.featureone.domain

import kotlinx.coroutines.flow.Flow

interface SomeRepository {
    fun getData(): Flow<String>
}