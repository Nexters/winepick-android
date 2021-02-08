package kr.co.nexters.winepick.base

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kakao.auth.KakaoSDK
import io.mockk.*
import kr.co.nexters.winepick.WinePickApplication
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Android 를 base 로 하는 테스트 클래스
 *
 * @since v1.0.0 / 2021.02.08
 */
@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
abstract class AndroidBaseTest {
    @Before
    fun setupAndroidBase() {
        WinePickApplication.appContext = ApplicationProvider.getApplicationContext()

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
