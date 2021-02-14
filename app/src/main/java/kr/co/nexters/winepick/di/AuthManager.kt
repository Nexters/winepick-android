package kr.co.nexters.winepick.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import timber.log.Timber
import java.util.*

/**
 *  Koin을 사용한 AuthModule
 */
class AuthManager(context: Context) {

    private val preferences = context.getSharedPreferences(
        AUTH_PREFERENCES,
        Context.MODE_PRIVATE
    )

    var token: String
        get() {
            return preferences.getString(TOKEN_KEY, null).orEmpty()
        }
        set(value) {
            preferences.edit {
                putString(TOKEN_KEY, value)
            }
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

    var test_type: String
        get(){
            return preferences.getString(TEST,null).orEmpty()
        }
        set(value){
            preferences.edit{
                putString(TEST, value)
            }
        }


    private companion object {
        const val AUTH_PREFERENCES = "auth"
        const val TOKEN_KEY = "token"
        const val AUTO_LOGIN_KEY = "auto"
        const val FIRST_KEY = "first"
        const val EXPIRE_KEY = "expire"
        const val TEST = "test"
    }
}
