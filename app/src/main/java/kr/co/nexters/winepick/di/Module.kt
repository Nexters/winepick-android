package kr.co.nexters.winepick.di

import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.network.WinePickService
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeViewModel
import kr.co.nexters.winepick.ui.type.TypeDetailModel
import kr.co.nexters.winepick.ui.like.LikeViewModel
import kr.co.nexters.winepick.ui.login.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val netModule = module {

    val httpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    val baseUrl = "http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/"

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideRetrofit(get()) }
}

val apiModule = module {
    fun provideApiService(retrofit: Retrofit): WinePickService {
        return retrofit.create(WinePickService::class.java)
    }

    single { provideApiService(get()) }
}

val viewModelModule = module {
    viewModel { BaseViewModel() }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { TypeDetailModel(get(), get()) }
    viewModel { LikeViewModel(get(), get()) }

}

val repositoryModule = module {
    single { WinePickRepository(get()) }
}

val authModule = module {
    single { AuthManager(get())  }
}

val moduleList = listOf(
    netModule,
    viewModelModule,
    repositoryModule,
    apiModule,
    authModule
)