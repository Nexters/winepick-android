package kr.co.nexters.winepick.base

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kakao.auth.KakaoSDK
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.repository.TestRepository
import kr.co.nexters.winepick.data.source.WineDataSource
import kr.co.nexters.winepick.di.moduleList
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.annotation.Config

/**
 * Android 를 base 로 하는 테스트 클래스
 *
 * @since v1.0.0 / 2021.02.08
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
abstract class AndroidBaseTest : KoinTest {
    val windDataSource: WineDataSource by inject()
    val testRepository: TestRepository by inject()

    private val mockModule = moduleList

    @Before
    fun setupAndroidBase() {
        WinePickApplication.appContext = ApplicationProvider.getApplicationContext()
        startKoin { modules(mockModule) }

        println("${this::class.java.canonicalName} mockking start")
        mockkStatic(KakaoSDK::class)
        every { KakaoSDK.init(any()) } returns Unit.apply {
            println("KakaoSDK 세팅 내용 mockking 처리")
        }
    }

    @After
    fun tearDownAndroidBase() {
        unmockkStatic(KakaoSDK::class)
        stopKoin()
    }
}
