package no.helge.android.featuretwo.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import no.helge.android.data.retrofit.flow.FlowCallAdapterFactory
import no.helge.android.featuretwo.domain.SomeRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class SomeRepositoryImpl @Inject constructor(): SomeRepository {

    override fun getData(): Flow<String> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://httpbin.org/")
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .build()

        val apiService = retrofit.create(SomeApi::class.java)

        return apiService
            .getBin()
            .map { it.origin }
            .flowOn(Dispatchers.IO)
    }
}
