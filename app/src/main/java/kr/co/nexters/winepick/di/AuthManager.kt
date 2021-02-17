package kr.co.nexters.winepick.di

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kr.co.nexters.winepick.data.model.LikeWine
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

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
    var id : Int
        get(){
            return preferences.getInt(ID,0)
        }
        set(value){
            preferences.edit{
                putInt(ID,value)
            }
        }


    private companion object {
        const val AUTH_PREFERENCES = "auth"
        const val TOKEN_KEY = "token"
        const val AUTO_LOGIN_KEY = "auto"
        const val FIRST_KEY = "first"
        const val EXPIRE_KEY = "expire"
        const val TEST = "test"
        const val RECENT_SEARCH = "search"
        const val ID = "id"
    }
}
