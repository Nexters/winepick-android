package kr.co.nexters.winepick.local

import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.repository.SearchRepository
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * 검색 화면에서의 Test
 *
 * @since v1.0.0 / 2021.02.09
 */
@HiltAndroidTest
class SearchTest : AndroidBaseTest() {
    @After
    fun tearDown() {
        sharedPrefs.clear()
    }

    /** [SearchRepository.getWineInfosLikeQuery] 테스트 */
    @Test
    fun searchRelativeTest() = runBlocking {
        val filteredWineInfos = searchRepository.getWineInfosLikeQuery("M")
        println(filteredWineInfos)
        Assert.assertNotEquals(filteredWineInfos.size, 0)

        val filteredEmptyWineInfos = searchRepository.getWineInfosLikeQuery("OX")
        println(filteredEmptyWineInfos)
        Assert.assertEquals(filteredEmptyWineInfos.size, 0)
    }

    /** [searchRepository.stylingWineInfos] 테스트 */
    @Test
    fun searchRelativeTestStyling() = runBlocking {
        val filteredWineInfos = searchRepository.getWineInfosLikeQuery("M")
        println(filteredWineInfos)
        searchRepository.stylingWineInfos(filteredWineInfos, "M").forEach {
            print("$it, ")
            it.matches("<.+?>".toRegex())
        }

        val filteredWineInfosLowerCase = searchRepository.getWineInfosLikeQuery("m")
        println(filteredWineInfosLowerCase)
        searchRepository.stylingWineInfos(filteredWineInfosLowerCase, "m").forEach {
            print("$it, ")
            it.matches("<.+?>".toRegex())
        }

        val filteredEmptyWineInfos = searchRepository.getWineInfosLikeQuery("OX")
        println(filteredEmptyWineInfos)
        searchRepository.stylingWineInfos(filteredEmptyWineInfos, "M").forEach {
            Assert.assertTrue(false)
        }
        Assert.assertEquals(filteredEmptyWineInfos.size, 0)
    }

    @Test
    fun searchFilterItemTest() {
        val abcd = searchRepository.searchFilterItems.groupBy { it.category }
        abcd.keys.sortedBy { it.ordinal }
        print(abcd)
    }
}
