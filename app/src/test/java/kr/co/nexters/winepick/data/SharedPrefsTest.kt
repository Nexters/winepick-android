package kr.co.nexters.winepick.data

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
        SharedPrefs.clear()
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
        println(SharedPrefs[stringKey, "abcd"])
        println(SharedPrefs[intKey, 3])
        println(SharedPrefs[longKey, 3L])
        println(SharedPrefs[floatKey, 5f])
        println(SharedPrefs[booleanKey, true])
        println()

        println("set & get print")
        SharedPrefs[stringKey] = "abcd"
        SharedPrefs[intKey] = 3
        SharedPrefs[longKey] = 3L
        SharedPrefs[floatKey] = 5f
        SharedPrefs[booleanKey] = true
        println(SharedPrefs[stringKey, "efgh"])
        println(SharedPrefs[intKey, 33333])
        println(SharedPrefs[longKey, 333L])
        println(SharedPrefs[floatKey, 533f])
        println(SharedPrefs[booleanKey, false])
        println()

        println("getAll print")
        println(SharedPrefs.all)
        println()

        println("minusAssign & contains print")
        SharedPrefs -= stringKey
        println(stringKey in SharedPrefs)
        println(intKey in SharedPrefs)
        println(longKey in SharedPrefs)
        println(floatKey in SharedPrefs)
        println(booleanKey in SharedPrefs)
        println()

        println("clear print")
        SharedPrefs.clear()
        println(SharedPrefs[stringKey, "a"])
        println(SharedPrefs[intKey, -1])
        println(SharedPrefs[longKey, 0L])
        println(SharedPrefs[floatKey, 0.0F])
        println(SharedPrefs[booleanKey, false])
    }

    @Test
    fun sharedPrefTest() = runBlocking {
        // default Value print
        Assert.assertTrue("abcd" == SharedPrefs[stringKey, "abcd"])
        Assert.assertTrue(3 == SharedPrefs[intKey, 3])
        Assert.assertTrue(3L == SharedPrefs[longKey, 3L])
        Assert.assertTrue(5f == SharedPrefs[floatKey, 5f])
        Assert.assertTrue(true == SharedPrefs[booleanKey, true])

        // set & get test
        SharedPrefs[stringKey] = "abcd"
        SharedPrefs[intKey] = 3
        SharedPrefs[longKey] = 3L
        SharedPrefs[floatKey] = 5f
        SharedPrefs[booleanKey] = true
        Assert.assertEquals("abcd", SharedPrefs[stringKey, "efgh"])
        Assert.assertEquals(3, SharedPrefs[intKey, 33333])
        Assert.assertEquals(3L, SharedPrefs[longKey, 333L])
        Assert.assertEquals(5f, SharedPrefs[floatKey, 533f])
        Assert.assertEquals(true, SharedPrefs[booleanKey, false])

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
                    Assert.assertEquals(SharedPrefs[it.key, "efgh"], it.value)
                }
                is Int -> {
                    Assert.assertEquals(SharedPrefs[it.key, 33333], it.value)
                }
                is Long -> {
                    Assert.assertEquals(SharedPrefs[it.key, 333L], it.value)
                }
                is Float -> {
                    Assert.assertEquals(SharedPrefs[it.key, 533f], it.value)
                }
                is Boolean -> {
                    Assert.assertEquals(SharedPrefs[it.key, false], it.value)
                }
            }

        }

        // minusAssign & contains test
        SharedPrefs -= stringKey
        Assert.assertFalse(stringKey in SharedPrefs)
        Assert.assertTrue(intKey in SharedPrefs)
        Assert.assertTrue(longKey in SharedPrefs)
        Assert.assertTrue(floatKey in SharedPrefs)
        Assert.assertTrue(booleanKey in SharedPrefs)
        println()

        // clear test
        SharedPrefs.clear()
        Assert.assertEquals("a", SharedPrefs[stringKey, "a"])
        Assert.assertEquals(-1, SharedPrefs[intKey, -1])
        Assert.assertEquals(0L, SharedPrefs[longKey, 0L])
        Assert.assertEquals(0.0F, SharedPrefs[floatKey, 0.0F])
        Assert.assertEquals(false, SharedPrefs[booleanKey, false])
    }
}
