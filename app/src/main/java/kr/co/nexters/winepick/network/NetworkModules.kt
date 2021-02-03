package kr.co.nexters.winepick.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.BuildConfig
import kr.co.nexters.winepick.WinePickApplication
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Retrofit2 를 사용하기 위한 클래스
 * TODO Hilt 를 사용한다면 아래 내용은 DI 를 사용하는 형태로 수정될 수 있음
 *
 * @since v1.0.0 / 2021.02.04
 */
object NetworkModules {
    const val CONNECT_TIMEOUT = 15
    const val WRITE_TIMEOUT = 15
    const val READ_TIMEOUT = 15

    private val cache = provideOkHttpCache(WinePickApplication.appContext)
    private val interceptor = provideInterceptor(null)
    private val loggingInterceptor = provideLoggingInterceptor()
    private val okHttpClient = provideOkHttpClient(cache, interceptor, loggingInterceptor)

    @JvmStatic
    val retrofit = provideWinePickService(okHttpClient)

    @JvmStatic
    val testRetrofit = provideTestService(okHttpClient)

    private fun provideOkHttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024L           // 10MB
        return Cache(context.cacheDir, cacheSize)
    }

    /**
     * 커스텀 interceptor
     * Firebase Crashlytic 로깅 로직을 넣을 예정이며 카카오 token 체크가 필요할 시 아래 함수를 활용한다.
     */
    private fun provideInterceptor(prefs: String?): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            if (prefs?.isNotEmpty() == true) {
//                val bearer = "BEARER $prefs"
                val builder = chain.request().newBuilder()
//                    .header("Authorization", bearer)
//                    .header("Accept", "application/json")
                return@Interceptor chain.proceed(builder.build())
            } else {
                return@Interceptor chain.proceed(chain.request())
            }
        }
    }

    /** 로깅 interceptor */
    private fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /** OkHttpClient constructor */
    private fun provideOkHttpClient(
        cache: Cache?,
        interceptor: Interceptor,
        loggingInterceptor: Interceptor
    ): OkHttpClient? {
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /** 실제 서비스에서 사용하는 Retrofit2 Service */
    private fun provideWinePickService(okHttpClient: OkHttpClient?): WinePickService {
        return Retrofit.Builder()
            .baseUrl("http://padakpadak.run.goorm.io/")
            .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(WinePickService::class.java)
    }

    /** UnitTest 에서 사용하는 Retrofit2 Service */
    private fun provideTestService(okHttpClient: OkHttpClient?): TestService {
        return Retrofit.Builder()
            .baseUrl("http://padakpadak.run.goorm.io/")
            .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(okHttpClient)
            .build()
            .create(TestService::class.java)
    }
}
