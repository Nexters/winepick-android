package kr.co.nexters.winepick.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.source.WineDataSource

/**
 * Wine 관련 Repository
 *
 * @since v1.0.0 / 2021.02.04
 */
object WineRepository {
    /** [WineDataSource] 를 통해, 와인 목록 [List] 을 가져온다. */
    suspend fun getWines(
        accessToken: String,
        pageSize: Int,
        pageNumber: Int = 0,
        sort: String = "",
    ) = withContext(Dispatchers.IO) {
        return@withContext WineDataSource.getWines(accessToken, pageSize, pageNumber, sort)
    }

    /** [WineDataSource] 를 통해, 특정 와인에 대한 정보 [WineResult] 를 가져온다. */
    suspend fun getWine(accessToken: String, wineId: Int) = withContext(Dispatchers.IO) {
        return@withContext WineDataSource.getWine(accessToken, wineId)
    }

    /** [WineDataSource] 를 통해, 필터링을 걸쳐 가져온 와인 목록 [List] 를 가져온다. */
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
    ) = withContext(Dispatchers.IO) {
        return@withContext WineDataSource.getWinesFilter(
            accessToken,
            wineName,
            category,
            food,
            store,
            start,
            end,
            keywords,
            pageSize,
            pageNumber,
            sort
        )
    }
}
