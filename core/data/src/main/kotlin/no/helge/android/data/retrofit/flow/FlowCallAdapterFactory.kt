package no.helge.android.data.retrofit.flow

import java.lang.reflect.*
import kotlinx.coroutines.flow.*
import no.helge.android.data.retrofit.flow.internal.BodyCallAdapter
import no.helge.android.data.retrofit.flow.internal.ResponseCallAdapter
import retrofit2.*

class FlowCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (Flow::class.java != getRawType(returnType)) {
            return null
        }

        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                "Flow return type must be parameterized as Flow<Foo>"
            )
        }

        val responseType = getParameterUpperBound(0, returnType)
        val rawDeferredType = getRawType(responseType)

        return if (rawDeferredType == Response::class.java) {
            if (responseType !is ParameterizedType) {
                throw IllegalStateException(
                    "Response must be parameterized as Response<Foo> or Response<out Foo>"
                )
            }
            ResponseCallAdapter<Any>(getParameterUpperBound(0, responseType))
        } else {
            BodyCallAdapter<Any>(responseType)
        }
    }
}