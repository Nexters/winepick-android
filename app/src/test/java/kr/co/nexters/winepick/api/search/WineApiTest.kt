package kr.co.nexters.winepick.api.search

import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.source.WineDataSource
import org.junit.Assert
import org.junit.Test

/**
 * 검색 관련 api 테스트
 *
 * @since v1.0.0 / 2021.02.09
 */
class WineApiTest : AndroidBaseTest() {
    /** [WineDataSource.getWines] 테스트 */
    @Test
    fun getWinesTest() = runBlocking {
        val result = WineDataSource.getWines("", 2)

        print("$result")

        Assert.assertNotNull(result)
    }

    /** [WineDataSource.getWine] 테스트 */
    @Test
    fun getWineTest() = runBlocking {
        val result = WineDataSource.getWine(2)

        print("$result")

        Assert.assertNotNull(result)
    }
}
