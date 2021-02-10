package kr.co.nexters.winepick.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.model.local.SearchCurrent
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.source.WineDataSource
import kr.co.nexters.winepick.util.SharedPrefs
import org.junit.*

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

    /** [SearchRepository.addSearchCurrent] 테스트 */
    @Test
    fun searchCurrentTest() = runBlocking {
        SearchRepository.addSearchCurrent(SearchCurrent(0, "ab"))
        println(SearchRepository.searchCurrents)
        SearchRepository.addSearchCurrent(SearchCurrent(1, "ac"))
        println(SearchRepository.searchCurrents)
        SearchRepository.addSearchCurrent(SearchCurrent(2, "ab"))

        Assert.assertEquals(SearchRepository.searchCurrents[0].time, 2L)
        Assert.assertEquals(SearchRepository.searchCurrents[0].value, "ab")
    }

    /** [SearchRepository.addSearchCurrent] 테스트 */
    @Test
    fun searchStyledCurrentTest() = runBlocking {
        SearchRepository.addSearchCurrent(SearchCurrent(0, "ab"))
        println(SearchRepository.searchCurrents)
        SearchRepository.addSearchCurrent(SearchCurrent(1, "ac"))
        println(SearchRepository.searchCurrents)

        SearchRepository.stylingSearchCurrent("a")
        println(SearchRepository.styledSearchCurrents.value)

        Assert.assertEquals(SearchRepository.styledSearchCurrents.value?.get(0)?.time, 1L)
        Assert.assertEquals(SearchRepository.styledSearchCurrents.value?.get(0)?.value, "<b>a</b>c")
    }

    /** [WineDataSource.getWine] 테스트 */
    @Test
    fun getWineTest() = runBlocking {
    }
}
