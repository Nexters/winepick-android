package kr.co.nexters.winepick.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.BuildConfig
import kr.co.nexters.winepick.WinePickApplication
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Retrofit2 를 사용하기 위한 클래스
 * TODO Hilt 를 사용한다면 아래 내용은 DI 를 사용하는 형태로 수정될 수 있음
 *
 * @since v1.0.0 / 2021.02.04
 */

@Module
@InstallIn (ApplicationComponent::class)
object NetworkModules {
    const val CONNECT_TIMEOUT = 15
    const val WRITE_TIMEOUT = 15
    const val READ_TIMEOUT = 15

    private val cache = provideOkHttpCache(WinePickApplication.getGlobalApplicationContext())
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
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(WinePickService::class.java)
    }

    /** UnitTest 에서 사용하는 Retrofit2 Service */
    private fun provideTestService(okHttpClient: OkHttpClient?): TestService {
        return Retrofit.Builder()
            .baseUrl("http://padakpadak.run.goorm.io/")
            .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(TestService::class.java)
    }

    /**
     * 코루틴을 활용하여 HTTP 요청을 보낼 시 활용하는 로직
     * 코루틴을 활용할 경우, onFailure 에서 보내는 exception 내용에 따라 로직 작업을 수행한다.
     */
    suspend fun <T> Call<T>.send(): Response<T> = suspendCoroutine {
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                it.resume(response)
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                it.resumeWithException(throwable)
            }
        })
    }
}
