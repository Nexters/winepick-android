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
        query: String,
        page: Int
    ): WinesResult? = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext getWinesResponse().result
        } else {
            val response = NetworkModules.retrofit.getWines(query, page).send()

            /** statusCode 별 처리 */
            when (response.code()) {

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
            val response = NetworkModules.retrofit.getWine(wineId).send()

            /** statusCode 별 처리 */
            when (response.code()) {

            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body()?.result
        }
    }
}
