package kr.co.nexters.winepick.api.search

import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.repository.WineRepository
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
        val result = WineDataSource.getWines(10, 0)

        println("$result")

        Assert.assertNotNull(result)

        val resultEmptyList = WineDataSource.getWines(20, 4)

        println("$resultEmptyList")

        Assert.assertNotNull(resultEmptyList)
    }

    /** [WineDataSource.getWine] 테스트 */
    @Test
    fun getWineTest() = runBlocking {
        val result = WineDataSource.getWine(2)

        print("$result")

        Assert.assertNotNull(result)
    }

    /** [WineDataSource.getWines] 테스트 */
    @Test
    fun getWinesFilterTest() = runBlocking {
        val resultTemp = WineRepository.getWinesFilter(
            wineName = "쁘띠폴리",
            keywords = listOf("달콤한"),
            size = 1
        )

        print("$resultTemp")

        Assert.assertNotNull(resultTemp)
//        // 다 채워져 있는 경우
//        // http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/v2/api/wine/filter%3FwineName=%EC%BD%98%EC%B0%A8%EC%9D%B4%ED%86%A0%EB%A1%9C%20%EC%9C%A0%EB%8B%88%EC%98%A8%2038%20%EB%A0%88%EC%84%B8%EB%A5%B4%EB%B0%94%20%EC%8B%9C%EB%9D%BC&category=%ED%99%94%EC%9D%B4%ED%8A%B8&food=%EA%B3%A0%EA%B8%B0&store=GS25&start=6&end=7&keyword=%EC%8C%89%EC%8B%B8%EB%A6%84%ED%95%9C&keyword=%ED%9C%B4%EC%8B%9D&keyword=%EA%B7%BC%ED%99%A9%20%ED%86%A0%ED%81%AC&size=0&page=1&sort=0
//        // decoded : http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/v2/api/wine/filter?wineName=콘차이토로 유니온 38 레세르바 시라&category=화이트&food=고기&store=GS25&start=6&end=7&keyword=쌉싸름한&keyword=휴식&keyword=근황 토크&size=0&page=1&sort=0
//        val resultFull = WineRepository.getWinesFilter(
//            wineName = "콘차이토로 유니온 38 레세르바 시라",
//            category = "화이트",
//            food = "고기",
//            store = "GS25",
//            start = 6,
//            end = 7,
//            keywords = listOf("쌉싸름한", "휴식", "근황 토크"),
//            size = 0,
//            page = 1,
//            sort = 0,
//        )
//
//        print("$resultFull")
//
//        Assert.assertNotNull(resultFull)
//
//        // 다 비어져 있는 경우
//        // http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/v2/api/wine/filter%3Fkeyword=&size=0&page=1&sort=0
//        // decoded : http://ec2-3-35-107-29.ap-northeast-2.compute.amazonaws.com:8080/v2/api/wine/filter?keyword=&size=0&page=1&sort=0
//        val resultEmpty = WineRepository.getWinesFilter(
//            size = 10,
//            page = 1,
//            sort = 0,
//        )
//
//        print("$resultEmpty")
//
//        Assert.assertNotNull(resultEmpty)
    }
}
