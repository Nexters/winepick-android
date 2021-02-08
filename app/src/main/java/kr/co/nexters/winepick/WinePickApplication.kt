package kr.co.nexters.winepick

import android.app.Application
import android.content.Context

/**
 * WinePick Application 클래스
 *
 * @author ricky
 * @since v1.0.0 / 2021.01.28
 */
class WinePickApplication : Application() {
    companion object {
        lateinit var appContext : Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}
