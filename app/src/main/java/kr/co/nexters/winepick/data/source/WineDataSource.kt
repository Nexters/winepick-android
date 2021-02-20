package kr.co.nexters.winepick.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.WinesResult
import kr.co.nexters.winepick.data.model.remote.wine.getWineResponse
import kr.co.nexters.winepick.data.model.remote.wine.getWinesResponse
import kr.co.nexters.winepick.network.NetworkModules
import kr.co.nexters.winepick.network.NetworkModules.send
import kr.co.nexters.winepick.network.WinePickService
import java.net.URLDecoder

/**
 * 와인 api 요청 시 사용하는 DataSource
 *
 * @since v1.0.0 / 2021.02.08
 */
object WineDataSource {
    /** 테스트 모드 flag 값. true 일 시 앱 내 mock 데이터 사용 */
    val isMock = false

    /** [WinePickService.getWines] 요청에 대한 비즈니스 로직 */
    suspend fun getWines(
        accessToken: String,
        pageSize: Int,
        pageNumber: Int = 0,
        sort: String = "",
    ): WinesResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWinesResponse().result
        } else {
            val response =
                NetworkModules.retrofit.getWines(accessToken, pageSize, pageNumber, sort).send()

            /** statusCode 별 처리 */
            when (response.code()) {

            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body()?.result
        }
    }

    /** [WinePickService.getWine] 요청에 대한 비즈니스 로직 */
    suspend fun getWine(
        accessToken: String, wineId: Int
    ): WineResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWineResponse().result
        } else {
            val response = NetworkModules.retrofit.getWine(accessToken, wineId).send()

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
     * @param pageSize 한번에 가져올 사이즈
     * @param pageNumber 해당 페이지
     * @param sort id 기준으로 내림차순/오름차순 정렬 가능 유무
     *
     * */
    suspend fun getWinesFilter(
        accessToken: String,
        wineName: String? = null,
        category: String? = null,
        food: String? = null,
        store: String? = null,
        start: Int? = null,
        end: Int? = null,
        keywords: List<String> = listOf(""),
        pageSize: Int,
        pageNumber: Int = 0,
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
            queryBuilder.append("pageSize=${pageSize}&")
            queryBuilder.append("pageNumber=${pageNumber}&")
            sort?.let { queryBuilder.append("sort=${sort}") }

            val response =
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M) {
                    NetworkModules.retrofit.getWinesFilter(
                        authorization = accessToken,
                        query = URLDecoder.decode(
                            queryBuilder.toString(),
                            java.nio.charset.StandardCharsets.UTF_8.toString()
                        )
                    ).send()

                } else {
                    NetworkModules.retrofit.getWinesFilter(
                        authorization = accessToken,
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
