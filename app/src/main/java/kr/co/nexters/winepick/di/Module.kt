package kr.co.nexters.winepick.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.BuildConfig
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.repository.TestRepository
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.data.source.SearchDataSource
import kr.co.nexters.winepick.data.source.WineDataSource
import kr.co.nexters.winepick.network.TestService
import kr.co.nexters.winepick.network.WinePickService
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeViewModel
import kr.co.nexters.winepick.ui.type.TypeDetailModel
import kr.co.nexters.winepick.ui.like.LikeViewModel
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.ui.search.SearchFilterViewModel
import kr.co.nexters.winepick.ui.search.SearchViewModel
import kr.co.nexters.winepick.util.SharedPrefs
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val CONNECT_TIMEOUT = 15.toLong()
const val WRITE_TIMEOUT = 15.toLong()
const val READ_TIMEOUT = 15.toLong()

const val BASE_URL = "http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/"
const val TEST_URL = "http://padakpadak.run.goorm.io/"

val netModule = module {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    fun provideOkHttpCache(): Cache {
        // 10MB
        val cacheSize = 10 * 1024 * 1024L
        return Cache(WinePickApplication.getGlobalApplicationContext().cacheDir, cacheSize)
    }

    /**
     * 커스텀 interceptor
     * Firebase Crashlytic 로깅 로직을 넣을 예정이며 카카오 token 체크가 필요할 시 아래 함수를 활용한다.
     */
    fun provideWinePickInterceptor(authManager: AuthManager): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            // wine/filter 인 경우, "?" 가 인코딩 되어있는지 확인 후 인코딩 풀어주기
            val request = chain.request()
            var newUrl = request.url.toString()
            if (newUrl.contains("v2/api/wine/filter"))
                newUrl = newUrl.replace("%3F", "?")

            //like 통신의 경우 accessToken header로 넣어주기
            if(newUrl.contains("v2/api/like")) {
                return@Interceptor chain.proceed(chain.request().newBuilder().apply {
                    addHeader("Authorization",authManager.token)
                    url(newUrl)
                }.build())
            }

            if(newUrl.contains("v2/api/wine")) {
                if(authManager.token != "guest") {
                    return@Interceptor chain.proceed(chain.request().newBuilder().apply {
                        addHeader("Authorization",authManager.token)
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
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideOkHttpCache() }
    single { provideWinePickInterceptor(get()) }
    single { provideHttpClient(get(), get()) }
    single { provideRetrofit(get()) }
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

val localModule = module {
    single { SharedPrefs() }
}

val apiModule = module {
    /** UnitTest 에서 사용하는 Retrofit2 Service */
    fun provideTestService(okHttpClient: OkHttpClient): TestService {
        return Retrofit.Builder()
            .baseUrl(TEST_URL)
            .addConverterFactory(Json.asConverterFactory("application/json; charset=utf-8".toMediaType()))
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(TestService::class.java)
    }

    fun provideApiService(retrofit: Retrofit): WinePickService {
        return retrofit.create(WinePickService::class.java)
    }

    single { provideTestService(get()) }
    single { provideApiService(get()) }
}

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TypeDetailModel(get(), get()) }
    viewModel { LikeViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(),get(), get()) }
    viewModel { SearchFilterViewModel(get()) }
}

val repositoryModule = module {
    single { WinePickRepository(get()) }
    single { TestRepository(get()) }
    single { WineRepository(get(), get()) }
    single { SearchRepository(get()) }
}

val dataSourceModule = module {
    single { WineDataSource(get()) }
    single { SearchDataSource(get()) }
}

val authModule = module {
    single { AuthManager(get()) }
}


val moduleList = listOf(
    netModule,
    viewModelModule,
    repositoryModule,
    dataSourceModule,
    localModule,
    apiModule,
    authModule
)
