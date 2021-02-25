package kr.co.nexters.winepick.di

import kr.co.nexters.winepick.constant.AuthConstant.AUTO_LOGIN_KEY
import kr.co.nexters.winepick.constant.AuthConstant.EXPIRE_KEY
import kr.co.nexters.winepick.constant.AuthConstant.ID
import kr.co.nexters.winepick.constant.AuthConstant.RECENT_SEARCH1
import kr.co.nexters.winepick.constant.AuthConstant.RECENT_SEARCH2
import kr.co.nexters.winepick.constant.AuthConstant.TEST
import kr.co.nexters.winepick.constant.AuthConstant.TOKEN_KEY
import kr.co.nexters.winepick.constant.AuthConstant.TYPE_NAME
import kr.co.nexters.winepick.util.SharedPrefs

/**
 *  Koin을 사용한 AuthModule
 */
class AuthManager(private val sharedPrefs: SharedPrefs) {
    var token: String
        get() {
            return sharedPrefs[TOKEN_KEY, ""] ?: ""
        }
        set(value) {
            sharedPrefs[TOKEN_KEY] = value
        }

    var autoLogin: Boolean
        get() {
            return sharedPrefs[AUTO_LOGIN_KEY, false] ?: false
        }
        set(value) {
            sharedPrefs[AUTO_LOGIN_KEY] = value
        }

    var expire: Long
        get() {
            return sharedPrefs[EXPIRE_KEY, 0] ?: 0
        }
        set(value) {
            sharedPrefs[EXPIRE_KEY] = value
        }

    var testType: String
        get() {
            return sharedPrefs[TEST, ""] ?: ""
        }
        set(value) {
            sharedPrefs[TEST] = value
        }
    var id: Int
        get() {
            return sharedPrefs[ID, 0] ?: 0
        }
        set(value) {
            sharedPrefs[ID] = value
        }
    var recentSearch1: String
        get() {
            return sharedPrefs[RECENT_SEARCH1, ""] ?: ""
        }
        set(value) {
            sharedPrefs[RECENT_SEARCH1] = value
        }

    var recentSearch2: String
        get() {
            return sharedPrefs[RECENT_SEARCH2, ""] ?: ""
        }
        set(value) {
            sharedPrefs[RECENT_SEARCH2] = value
        }

    var typeName: String
        get() {
            return sharedPrefs[TYPE_NAME,""] ?: ""
        }
        set(value) {
            sharedPrefs[TYPE_NAME] = value
        }
}
