package kr.co.nexters.winepick

import android.app.Application
import android.content.Context
import com.kakao.auth.*
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * WinePick Application 클래스
 *
 * @since v1.0.0 / 2021.01.28
 */

@HiltAndroidApp
class WinePickApplication : Application() {
    companion object {
        var appContext : Context? = null

        fun getGlobalApplicationContext(): Context {
            checkNotNull(appContext) { "This Application does not inherit com.kakao.GlobalApplication" }
            return appContext!!
        }

        fun getGlobalAppApplication(): WinePickApplication {
            checkNotNull(appContext) { "This Application does not inherit com.kakao.GlobalApplication" }
            return appContext!! as WinePickApplication
        }
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

}
