package kr.co.nexters.winepick.data

import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.util.SharedPrefs
import org.junit.After
import org.junit.Assert
import org.junit.Test

/**
 * SharedPrefs 테스트
 *
 * @since v1.0.0 / 2021.02.03
 */
@HiltAndroidTest
class SharedPrefsTest : AndroidBaseTest() {
    companion object {
        val stringKey = "StringKey"
        val intKey = "IntKey"
        val longKey = "LongKey"
        val floatKey = "FloatKey"
        val booleanKey = "BooleanKey"
    }

    @After
    fun tearDown(){
        // 프리퍼런스 초기화
        sharedPrefs.clear()
    }

    @Test
    fun sharedPrefPrint() = runBlocking {
        println("key name print")
        println(stringKey)
        println(intKey)
        println(longKey)
        println(floatKey)
        println(booleanKey)
        println()

        println("default Value print")
        println(sharedPrefs[stringKey, "abcd"])
        println(sharedPrefs[intKey, 3])
        println(sharedPrefs[longKey, 3L])
        println(sharedPrefs[floatKey, 5f])
        println(sharedPrefs[booleanKey, true])
        println()

        println("set & get print")
        sharedPrefs[stringKey] = "abcd"
        sharedPrefs[intKey] = 3
        sharedPrefs[longKey] = 3L
        sharedPrefs[floatKey] = 5f
        sharedPrefs[booleanKey] = true
        println(sharedPrefs[stringKey, "efgh"])
        println(sharedPrefs[intKey, 33333])
        println(sharedPrefs[longKey, 333L])
        println(sharedPrefs[floatKey, 533f])
        println(sharedPrefs[booleanKey, false])
        println()

        println("getAll print")
        println(sharedPrefs.all)
        println()

        println("minusAssign & contains print")
        sharedPrefs -= stringKey
        println(stringKey in sharedPrefs)
        println(intKey in sharedPrefs)
        println(longKey in sharedPrefs)
        println(floatKey in sharedPrefs)
        println(booleanKey in sharedPrefs)
        println()

        println("clear print")
        sharedPrefs.clear()
        println(sharedPrefs[stringKey, "a"])
        println(sharedPrefs[intKey, -1])
        println(sharedPrefs[longKey, 0L])
        println(sharedPrefs[floatKey, 0.0F])
        println(sharedPrefs[booleanKey, false])
    }

    @Test
    fun sharedPrefTest() = runBlocking {
        // default Value print
        Assert.assertTrue("abcd" == sharedPrefs[stringKey, "abcd"])
        Assert.assertTrue(3 == sharedPrefs[intKey, 3])
        Assert.assertTrue(3L == sharedPrefs[longKey, 3L])
        Assert.assertTrue(5f == sharedPrefs[floatKey, 5f])
        Assert.assertTrue(true == sharedPrefs[booleanKey, true])

        // set & get test
        sharedPrefs[stringKey] = "abcd"
        sharedPrefs[intKey] = 3
        sharedPrefs[longKey] = 3L
        sharedPrefs[floatKey] = 5f
        sharedPrefs[booleanKey] = true
        Assert.assertEquals("abcd", sharedPrefs[stringKey, "efgh"])
        Assert.assertEquals(3, sharedPrefs[intKey, 33333])
        Assert.assertEquals(3L, sharedPrefs[longKey, 333L])
        Assert.assertEquals(5f, sharedPrefs[floatKey, 533f])
        Assert.assertEquals(true, sharedPrefs[booleanKey, false])

        // getAll test
        val answerMap = mapOf(
            stringKey to "abcd",
            intKey to 3,
            longKey to 3L,
            floatKey to 5f,
            booleanKey to true
        )
        answerMap.forEach {
            when (it.value) {
                is String -> {
                    Assert.assertEquals(sharedPrefs[it.key, "efgh"], it.value)
                }
                is Int -> {
                    Assert.assertEquals(sharedPrefs[it.key, 33333], it.value)
                }
                is Long -> {
                    Assert.assertEquals(sharedPrefs[it.key, 333L], it.value)
                }
                is Float -> {
                    Assert.assertEquals(sharedPrefs[it.key, 533f], it.value)
                }
                is Boolean -> {
                    Assert.assertEquals(sharedPrefs[it.key, false], it.value)
                }
            }

        }

        // minusAssign & contains test
        sharedPrefs -= stringKey
        Assert.assertFalse(stringKey in sharedPrefs)
        Assert.assertTrue(intKey in sharedPrefs)
        Assert.assertTrue(longKey in sharedPrefs)
        Assert.assertTrue(floatKey in sharedPrefs)
        Assert.assertTrue(booleanKey in sharedPrefs)
        println()

        // clear test
        sharedPrefs.clear()
        Assert.assertEquals("a", sharedPrefs[stringKey, "a"])
        Assert.assertEquals(-1, sharedPrefs[intKey, -1])
        Assert.assertEquals(0L, sharedPrefs[longKey, 0L])
        Assert.assertEquals(0.0F, sharedPrefs[floatKey, 0.0F])
        Assert.assertEquals(false, sharedPrefs[booleanKey, false])
    }
}
