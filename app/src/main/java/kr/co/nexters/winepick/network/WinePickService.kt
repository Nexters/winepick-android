package kr.co.nexters.winepick.network

import kr.co.nexters.winepick.data.model.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 실제 서비스에서 사용하는 Retrofit2 인터페이스
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.04
 */
interface WinePickService {
    // TODO 검색 결과 api
    @GET
    fun getSearchResults(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<List<SearchResult>>
}
