package kr.co.nexters.winepick

import android.app.Application
import android.content.Context
import com.kakao.auth.*
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import kr.co.nexters.winepick.WinePickApplication.Companion.getGlobalApplicationContext
import kr.co.nexters.winepick.di.moduleList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * WinePick Application 클래스
 *
 * @author ricky
 * @since v1.0.0 / 2021.01.28
 */

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
        appContext = this
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
        initTimber()
        startKoinModules()
    }

    private fun startKoinModules() {
        startKoin {
            androidContext(this@WinePickApplication)
            modules(moduleList)
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

}
