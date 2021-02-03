package kr.co.nexters.winepick.network

import kr.co.nexters.winepick.feature.riflockle7.DummyDetailMovie
import kr.co.nexters.winepick.feature.riflockle7.DummyMovieCommentResponse
import kr.co.nexters.winepick.feature.riflockle7.DummyMovieResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * UnitTest 에서 사용하는 Retrofit2 인터페이스
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.04
 */
interface TestService {
    @GET("movies/")
    fun getMovies(
        @Query("order_type") orderType: Int
    ): Call<DummyMovieResponse>

    @GET("movie/")
    fun getMovie(
        @Query("id") id: String
    ): Call<DummyDetailMovie>

    @POST("comment/")
    fun postComment(
        @Body data: RequestBody
    ): Call<DummyMovieCommentResponse>
}
