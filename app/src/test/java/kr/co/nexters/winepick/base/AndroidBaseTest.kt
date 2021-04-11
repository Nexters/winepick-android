package kr.co.nexters.winepick.base

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.android.datatransport.runtime.dagger.Binds
import com.google.android.datatransport.runtime.dagger.Module
import com.kakao.auth.KakaoSDK
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.repository.SurveyRepository
import kr.co.nexters.winepick.data.repository.TestRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.data.source.WineDataSource
import kr.co.nexters.winepick.di.*
import kr.co.nexters.winepick.util.SharedPrefs
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

/**
 * Android 를 base 로 하는 테스트 클래스
 *
 * @since v1.0.0 / 2021.02.08
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
abstract class AndroidBaseTest {
    @Rule
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule
    @JvmField
    val liveDataRule = InstantTaskExecutorRule()

    @Inject
    lateinit var testRepository: TestRepository

    @Inject
    lateinit var searchRepository: SearchRepository

    @Inject
    lateinit var wineRepository: WineRepository

    @Inject
    lateinit var surveyRepository: SurveyRepository

    @Inject
    lateinit var wineDataSource: WineDataSource

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @Before
    fun setupAndroidBase() {
        WinePickApplication.appContext = ApplicationProvider.getApplicationContext()
        hiltRule.inject()

        println("${this::class.java.canonicalName} mockking start")
        mockkStatic(KakaoSDK::class)
        every { KakaoSDK.init(any()) } returns Unit.apply {
            println("KakaoSDK 세팅 내용 mockking 처리")
        }
    }

    @After
    fun tearDownAndroidBase() {
        unmockkStatic(KakaoSDK::class)
    }
}
