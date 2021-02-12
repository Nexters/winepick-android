package kr.co.nexters.winepick.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.util.SharedPrefs
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * 검색 화면에서의 Test
 *
 * @since v1.0.0 / 2021.02.09
 */
class SearchTest : AndroidBaseTest() {
    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        SharedPrefs.clear()
    }

    /** [SearchRepository.getWineInfosLikeQuery] 테스트 */
    @Test
    fun searchRelativeTest() = runBlocking {
        val filteredWineInfos = SearchRepository.getWineInfosLikeQuery("M")
        println(filteredWineInfos)
        Assert.assertNotEquals(filteredWineInfos.size, 0)

        val filteredEmptyWineInfos = SearchRepository.getWineInfosLikeQuery("OX")
        println(filteredEmptyWineInfos)
        Assert.assertEquals(filteredEmptyWineInfos.size, 0)
    }

    /** [SearchRepository.stylingWineInfos] 테스트 */
    @Test
    fun searchRelativeTestStyling() = runBlocking {
        val filteredWineInfos = SearchRepository.getWineInfosLikeQuery("M")
        println(filteredWineInfos)
        SearchRepository.stylingWineInfos(filteredWineInfos, "M").forEach {
            print("$it, ")
            it.matches("<.+?>".toRegex())
        }

        val filteredWineInfosLowerCase = SearchRepository.getWineInfosLikeQuery("m")
        println(filteredWineInfosLowerCase)
        SearchRepository.stylingWineInfos(filteredWineInfosLowerCase, "m").forEach {
            print("$it, ")
            it.matches("<.+?>".toRegex())
        }

        val filteredEmptyWineInfos = SearchRepository.getWineInfosLikeQuery("OX")
        println(filteredEmptyWineInfos)
        SearchRepository.stylingWineInfos(filteredEmptyWineInfos, "M").forEach {
            Assert.assertTrue(false)
        }
        Assert.assertEquals(filteredEmptyWineInfos.size, 0)
    }

    @Test
    fun searchFilterItemTest() {
        val abcd = SearchRepository.searchFilterItems.groupBy { it.category }
        abcd.keys.sortedBy { it.ordinal }
        print(abcd)
    }
}
