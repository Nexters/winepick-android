package kr.co.nexters.winepick.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.SearchResult
import kr.co.nexters.winepick.data.model.getSearchResultDummy
import kr.co.nexters.winepick.network.NetworkModules
import kr.co.nexters.winepick.network.NetworkModules.send

/**
 * 검색 api 요청 시 사용하는 DataSource
 *
 * @since v1.0.0 / 2021.02.08
 */
object SearchDataSource {
    val isMock = true

    suspend fun getSearchResults(
        query: String, page: Int
    ): List<SearchResult> = withContext(Dispatchers.IO) {
        if (isMock) {
            return@withContext mutableListOf(
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
                getSearchResultDummy(),
            )
        } else {
            val response = NetworkModules.retrofit.getSearchResults(query, page).send()

            /** statusCode 별 처리 */
            when (response.code()) {

            }

            if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

            return@withContext response.body() ?: listOf()
        }
    }
}
