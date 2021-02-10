package kr.co.nexters.winepick.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.wine.WineResult
import kr.co.nexters.winepick.data.source.WineDataSource

/**
 * Wine 관련 Repository
 *
 * @since v1.0.0 / 2021.02.04
 */
object WineRepository {
    /** [WineDataSource] 를 통해, 와인 목록 [List] 을 가져온다. */
    suspend fun getWines(query: String, page: Int) = withContext(Dispatchers.IO) {
        return@withContext WineDataSource.getWines(query, page)
    }

    /** [WineDataSource] 를 통해, 특정 와인에 대한 정보 [WineResult] 를 가져온다. */
    suspend fun getWine(wineId: Int) = withContext(Dispatchers.IO) {
        return@withContext WineDataSource.getWine(wineId)
    }
}
