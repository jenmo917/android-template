package no.helge.android.featuretwo.domain

import kotlinx.coroutines.flow.Flow

interface SomeRepository {
    fun getData(): Flow<String>
}