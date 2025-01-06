package no.helge.android.common.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T> T.asFlow(): Flow<T> = flowOf(this)