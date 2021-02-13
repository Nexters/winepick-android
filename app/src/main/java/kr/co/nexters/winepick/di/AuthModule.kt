package kr.co.nexters.winepick

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import kr.co.nexters.winepick.constant.authConstant.Companion.AUTH_PREFERENCES
import kr.co.nexters.winepick.constant.authConstant.Companion.AUTO_LOGIN_KEY
import kr.co.nexters.winepick.constant.authConstant.Companion.EXPIRE_KEY
import kr.co.nexters.winepick.constant.authConstant.Companion.FIRST_KEY
import kr.co.nexters.winepick.constant.authConstant.Companion.TOKEN_KEY
import kr.co.nexters.winepick.ui.base.BaseActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
object AuthModule{

    private val preferences =  WinePickApplication.getGlobalApplicationContext().getSharedPreferences(
            AUTH_PREFERENCES,
            Context.MODE_PRIVATE
    )

    var token: String
        get() {
            return preferences.getString(TOKEN_KEY,null).orEmpty()
        }
        set(value) {
            preferences.edit {
                putString(TOKEN_KEY, value)
            }
        }


    @JvmName("getToken1")
    @Provides
    fun getToken(): String {
        return token
    }

    var autoLogin: Boolean
        get() {
            return preferences.getBoolean(AUTO_LOGIN_KEY, false)
        }
        set(value) {
            preferences.edit {
                putBoolean(AUTO_LOGIN_KEY, value)
            }
        }

    var first: Boolean
        get() {
            return preferences.getBoolean(FIRST_KEY, false)
        }
        set(value) {
            preferences.edit {
                putBoolean(FIRST_KEY, value)
            }
        }

    var expire: Long
        get(){
            return preferences.getLong(EXPIRE_KEY,0)
        }
        set(value){
            preferences.edit{
                putLong(EXPIRE_KEY, value)
            }
        }

}