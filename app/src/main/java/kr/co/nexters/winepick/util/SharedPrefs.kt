package kr.co.nexters.winepick.util

import android.app.Activity
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.viewbinding.BuildConfig
import kr.co.nexters.winepick.BuildConfig.APPLICATION_ID
import kr.co.nexters.winepick.WinePickApplication
import org.jetbrains.annotations.NotNull

/**
 * SharedPreference object class
 *
 * @since v1.0.0 / 2021.02.03
 */
object SharedPrefs {
    private val sharedPreferences: SharedPreferences by lazy {
        WinePickApplication.appContext.getSharedPreferences(
            kr.co.nexters.winepick.BuildConfig.APPLICATION_ID,
            Activity.MODE_PRIVATE
        )
    }

    private val editor: SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
    }

    /** 모든 저장된 값 가져오기 */
    val all: Map<String, *> get() = sharedPreferences.all

    /** 프리퍼런스에 [key]가 저장되어있는지 여부를 반환한다. */
    operator fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    /** 프리퍼런스의 [key]에 [값][value]을 쓴다. */
    operator fun <T> set(key: String, @NotNull value: T) {
        try {
            when (value) {
                is String -> {
                    editor.putString(key, value as String)
                }
                is Int -> {
                    editor.putInt(key, value as Int)
                }
                is Long -> {
                    editor.putLong(key, value as Long)
                }
                is Float -> {
                    editor.putFloat(key, value as Float)
                }
                is Boolean -> {
                    editor.putBoolean(key, value as Boolean)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            editor.apply()
        }
    }

    /** 프리퍼런스의 [key]값에 대응하는 값을 반환하며, [defaultValue] 의 타입에 따라 그 값을 반환한다. */
    operator fun <T> get(key: String, @NotNull defaultValue: T): T? {
        when (defaultValue) {
            is String -> {
                return sharedPreferences.getString(key, defaultValue) as T
            }
            is Int -> {
                return sharedPreferences.getInt(key, defaultValue) as T
            }
            is Long -> {
                return sharedPreferences.getLong(key, defaultValue) as T
            }
            is Float -> {
                return sharedPreferences.getFloat(key, defaultValue) as T
            }
            is Boolean -> {
                return sharedPreferences.getBoolean(key, defaultValue) as T
            }
            else -> return null
        }
    }

    /** 프리퍼런스에서 [key]를 삭제한다. */
    operator fun minusAssign(key: String) {
        editor.remove(key).apply()
    }

    /**
     * 다음 테스트를 위해 프리퍼런스를 초기화한다. (테스트 코드 전용 메서드)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clear() {
        editor.clear().apply()
    }
}
