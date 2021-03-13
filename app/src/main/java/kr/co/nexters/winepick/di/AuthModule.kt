package kr.co.nexters.winepick

import android.content.Context
import androidx.core.content.edit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kr.co.nexters.winepick.constant.AuthConstant.AUTH_PREFERENCES
import kr.co.nexters.winepick.constant.AuthConstant.AUTO_LOGIN_KEY
import kr.co.nexters.winepick.constant.AuthConstant.EXPIRE_KEY
import kr.co.nexters.winepick.constant.AuthConstant.FIRST_KEY
import kr.co.nexters.winepick.constant.AuthConstant.TOKEN_KEY


// TODO Hilt 을 사용한 AuthModule
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
