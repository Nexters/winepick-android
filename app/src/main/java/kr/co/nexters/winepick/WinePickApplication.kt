package kr.co.nexters.winepick

import android.app.Application
import android.content.Context
import com.kakao.auth.*
import dagger.hilt.android.HiltAndroidApp
import kr.co.nexters.winepick.WinePickApplication.Companion.getGlobalApplicationContext
import timber.log.Timber

/**
 * WinePick Application 클래스
 *
 * @author ricky
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
    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        appContext = this
        KakaoSDK.init(KakaoSDKApapter())
    }

}
class KakaoSDKApapter : KakaoAdapter() {
    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig { getGlobalApplicationContext() }
    }

    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun getAuthTypes(): Array<AuthType> {
                return arrayOf(AuthType.KAKAO_LOGIN_ALL)
            }

            override fun isUsingWebviewTimer(): Boolean {
                return false
            }

            override fun isSecureMode(): Boolean {
                return false
            }

            override fun getApprovalType(): ApprovalType? {
                return ApprovalType.INDIVIDUAL
            }

            override fun isSaveFormData(): Boolean {
                return true
            }

        }
    }

}
