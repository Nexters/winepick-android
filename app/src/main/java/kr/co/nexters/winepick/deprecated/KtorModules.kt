package kr.co.nexters.winepick.deprecated

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * Ktor 프레임워크를 사용하는 모듈 클래스
 *
 * Ref
 * @see [KtoR 공식 문서](https://ktor.io/docs/client.html#creating-client)
 * @see [KtoR 사용 예제](https://medium.com/l-r-engineering/migrating-retrofit-to-ktor-93bdaf58d7d4)
 *
 * @since v1.0.0 / 2021.02.02
 */
object KtorModules {
    const val BASE_URL = "http://padakpadak.run.goorm.io"
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    fun createHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            // Json
            install(JsonFeature) { serializer = KotlinxSerializer(json) }

            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Ktor ", message)
                    }
                }
                level = LogLevel.ALL
            }

            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }

            // Apply to All Requests
            defaultRequest {
                parameter("api_key", "some_api_key")
                // Content Type
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            // Optional OkHttp Interceptors
            engine {
                // addInterceptor(CurlInterceptor(Loggable { Log.v("Curl", it) }))
            }
        }
    }
}
