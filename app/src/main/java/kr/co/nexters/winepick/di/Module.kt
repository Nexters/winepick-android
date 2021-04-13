package kr.co.nexters.winepick.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.BuildConfig
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.repository.*
import kr.co.nexters.winepick.data.source.SearchDataSource
import kr.co.nexters.winepick.data.source.SurveyDataSource
import kr.co.nexters.winepick.data.source.WineDataSource
import kr.co.nexters.winepick.network.TestService
import kr.co.nexters.winepick.network.WinePickService
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.util.SharedPrefs
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
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val CONNECT_TIMEOUT = 15.toLong()
const val WRITE_TIMEOUT = 15.toLong()
const val READ_TIMEOUT = 15.toLong()

const val BASE_URL = "http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/"
const val TEST_URL = "http://padakpadak.run.goorm.io/"

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

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun provideViewModelFactory(
        winePickRepository: WinePickRepository
    ): ViewModelProvider.AndroidViewModelFactory = ViewModelFactoryImpl(
        WinePickApplication.getGlobalAppApplication(), winePickRepository
    )

    /**
     * ViewModelFactory 구현체 (impl) 를 만드는 클래스
     */
    class ViewModelFactoryImpl(
        val application: WinePickApplication,
        val winePickRepository: WinePickRepository
    ) : ViewModelProvider.AndroidViewModelFactory(application) {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BaseViewModel(winePickRepository) as T
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpCache(): Cache {
        // 10MB
        val cacheSize = 10 * 1024 * 1024L
        return Cache(WinePickApplication.getGlobalApplicationContext().cacheDir, cacheSize)
    }

    /**
     * 커스텀 interceptor
     * Firebase Crashlytic 로깅 로직을 넣을 예정이며 카카오 token 체크가 필요할 시 아래 함수를 활용한다.
     */
    @Provides
    @Singleton
    fun provideWinePickInterceptor(authManager: AuthManager): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            // wine/filter 인 경우, "?" 가 인코딩 되어있는지 확인 후 인코딩 풀어주기
            val request = chain.request()
            var newUrl = request.url.toString()
            if (newUrl.contains("v2/api/wine/filter"))
                newUrl = newUrl.replace("%3F", "?")

            //like 통신의 경우 accessToken header로 넣어주기
            if (newUrl.contains("v2/api/like")) {
                return@Interceptor chain.proceed(chain.request().newBuilder().apply {
                    addHeader("Authorization", authManager.token)
                    url(newUrl)
                }.build())
            }

            if (newUrl.contains("v2/api/wine")) {
                if (authManager.token != "guest") {
                    return@Interceptor chain.proceed(chain.request().newBuilder().apply {
                        addHeader("Authorization", authManager.token)
                        url(newUrl)
                    }.build())
                }
            }

            val builder = chain.request().newBuilder()
                .url(newUrl)

            return@Interceptor chain.proceed(builder.build())
        }
    }

    /** HttpClient 객체를 생성하는 Provider 함수이다. */
    @Provides
    @Singleton
    fun provideHttpClient(okHttpCache: Cache, winePickInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(okHttpCache)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(winePickInterceptor)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    /** Retrofit 객체를 생성하는 Provider 함수이다. */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    /** UnitTest 에서 사용하는 Retrofit2 Service */
    @Provides
    @Singleton
    fun provideTestService(okHttpClient: OkHttpClient): TestService {
        return Retrofit.Builder()
            .baseUrl(TEST_URL)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(TestService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): WinePickService {
        return retrofit.create(WinePickService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideSharedPrefs(): SharedPrefs {
        return SharedPrefs()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthManager(sharedPrefs: SharedPrefs): AuthManager {
        return AuthManager(sharedPrefs)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTestRepository(testService: TestService): TestRepository {
        return TestRepository(testService)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(searchDataSource: SearchDataSource): SearchRepository {
        return SearchRepository(searchDataSource)
    }

    @Provides
    @Singleton
    fun provideWineRepository(
        wineDataSource: WineDataSource,
        sharedPrefs: SharedPrefs
    ): WineRepository {
        return WineRepository(wineDataSource, sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideWinePickRepository(
        winePickService: WinePickService,
        authManager: AuthManager
    ): WinePickRepository {
        return WinePickRepository(winePickService, authManager)
    }

    @Provides
    @Singleton
    fun provideSurveyRepository(
        surveyDataSource: SurveyDataSource,
        sharedPrefs: SharedPrefs
    ): SurveyRepository {
        return SurveyRepository(surveyDataSource, sharedPrefs)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideSearchDataSource(sharedPrefs: SharedPrefs): SearchDataSource {
        return SearchDataSource(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideWineDataSource(winePickService: WinePickService): WineDataSource {
        return WineDataSource(winePickService)
    }

    @Provides
    @Singleton
    fun provideSurveyDataSource(winePickService: WinePickService): SurveyDataSource {
        return SurveyDataSource(winePickService)
    }
}
