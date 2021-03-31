package kr.co.nexters.winepick.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.local.LastPageException
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.WinesResult
import kr.co.nexters.winepick.data.model.remote.wine.getWineResponse
import kr.co.nexters.winepick.data.model.remote.wine.getWinesResponse
import kr.co.nexters.winepick.di.send
import kr.co.nexters.winepick.network.WinePickService
import java.net.URLDecoder

/**
 * 와인 api 요청 시 사용하는 DataSource
 *
 * @since v1.0.0 / 2021.02.08
 */
class WineDataSource(private val winePickService: WinePickService) {
    /** 테스트 모드 flag 값. true 일 시 앱 내 mock 데이터 사용 */
    val isMock = false

    /** [WinePickService.getWines] 요청에 대한 비즈니스 로직 */
    suspend fun getWines(
        size: Int,
        page: Int = 0,
        sort: String = "",
    ): WinesResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWinesResponse().result
        } else {
            val response = winePickService.getWines(size, page, sort).send()

            /** statusCode 별 처리 */
            when (response.code()) {

            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body()?.result
        }
    }

    /** [WinePickService.getWines] 요청에 대한 비즈니스 로직 */
    suspend fun getWinesKeyword(
        size: Int,
        page: Int = 0,
        keyword: String
    ): WinesResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWinesResponse().result
        } else {
            val response = winePickService.getWinesKeyword(size, page, keyword).send()

            /** statusCode 별 처리 */
            when (response.code()) {
                500 -> throw LastPageException("${response.code()} API 오류")
            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body()?.result
        }
    }

    /** [WinePickService.getWine] 요청에 대한 비즈니스 로직 */
    suspend fun getWine(wineId: Int): WineResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWineResponse().result
        } else {
            val response = winePickService.getWine(wineId).send()

            /** statusCode 별 처리 */
            when (response.code()) {

            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body()?.result
        }
    }

    /**
     * [WinePickService.getWinesFilter] 요청에 대한 비즈니스 로직
     *
     * @param wineName 와인 이름 영문/한글 상관 없음
     * @param category 화이트/레드/스파클링
     * @param food 음식
     * @param store 편의점
     * @param start 도수 시작점
     * @param end 도수 끝
     * @param keywords 키워드
     * @param size 한번에 가져올 사이즈
     * @param page 해당 페이지
     * @param sort id 기준으로 내림차순/오름차순 정렬 가능 유무
     *
     * */
    suspend fun getWinesFilter(
        wineName: String? = null,
        category: String? = null,
        food: String? = null,
        store: String? = null,
        start: Int? = null,
        end: Int? = null,
        keywords: List<String> = listOf(""),
        size: Int,
        page: Int = 0,
        sort: String? = null,
    ): WinesResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWinesResponse().result
        } else {
            var queryBuilder = StringBuilder("?")

            wineName?.let { queryBuilder.append("wineName=${wineName}&") }
            category?.let { queryBuilder.append("category=${category}&") }
            food?.let { queryBuilder.append("food=${food}&") }
            store?.let { queryBuilder.append("store=${store}&") }
            start?.let { queryBuilder.append("start=${start}&") }
            end?.let { queryBuilder.append("end=${end}&") }
            keywords.forEach { keyword -> queryBuilder.append("keyword=${keyword}&") }
            queryBuilder.append("size=${size}&")
            queryBuilder.append("page=${page}&")
            sort?.let { queryBuilder.append("sort=${sort}") }

            val response =
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                    winePickService.getWinesFilter(
                        query = URLDecoder.decode(
                            queryBuilder.toString(),
                            java.nio.charset.StandardCharsets.UTF_8.toString()
                        )
                    ).send()

                } else {
                    winePickService.getWinesFilter(
                        query = queryBuilder.toString()
                    ).send()
                }

            /** statusCode 별 처리 */
            when (response.code()) {

            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body()?.result
        }
    }
}
