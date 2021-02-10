package kr.co.nexters.winepick.network

import kr.co.nexters.winepick.data.model.WinePickResponse
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.WinesResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 실제 서비스에서 사용하는 Retrofit2 인터페이스
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.04
 */
interface WinePickService {
    @GET("v2/api/wine")
    fun getWines(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<WinePickResponse<WinesResult>>

    @GET("v2/api/wine/{wine_id}")
    fun getWine(
        @Path("wine_id") wineId: Int,
    ): Call<WinePickResponse<WineResult>>
}
