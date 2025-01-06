package no.helge.android.featuretwo.data

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface SomeApi {
    @GET("get")
    fun getBin(): Flow<HttpBinResponse>
}

data class HttpBinResponse(
    val origin: String,
)