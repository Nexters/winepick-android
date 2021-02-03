package kr.co.nexters.winepick.api

import androidx.test.core.app.ApplicationProvider
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.network.NetworkModules
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.URL

/**
 * Retrofit2 테스트
 *
 * @since v1.0.0 / 2021.02.04
 */
@RunWith(RobolectricTestRunner::class)
class Retrofit2Test {
    @Before
    fun setup() {
        WinePickApplication.appContext = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun retrofit2Test() {
        // WHEN 1. "/movies" GET 요청 시
        val getMoviesResponse = NetworkModules.testRetrofit.getMovies(1).execute()
        val getMovies = getMoviesResponse.body()

        // THEN 1. 응답값이 일치해야 한다.
        Assert.assertEquals(getMovies?.movies?.size, 8)
        Assert.assertEquals(getMovies?.movies?.get(0)?.title, "꾼")
        Assert.assertEquals(getMovies?.movies?.get(0)?.userRating, 5)
        Assert.assertEquals(getMovies?.movies?.get(0)?.reservationRate, 61.69f)
        Assert.assertEquals(getMovies?.orderType, 1)

        // GIVEN 2. id 에 들어갈 파라미터 정의
        val movieId = "5a54c286e8a71d136fb5378e"
        // WHEN 2. "/movie" GET 요청 시
        val getMovieResponse = NetworkModules.testRetrofit.getMovie(movieId).execute()
        val getMovie = getMovieResponse.body()
        // THEN 2. 응답값이 일치해야 한다.
        Assert.assertEquals(getMovie?.actor, "하정우(강림), 차태현(자홍), 주지훈(해원맥), 김향기(덕춘)")
        Assert.assertEquals(
            getMovie?.image,
            URL("https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/padakpadak/1.jpg")
        )
        Assert.assertEquals(getMovie?.duration, 139)
        Assert.assertEquals(getMovie?.reservationRate, 35.5f)

        // GIVEN 3. body jsonObject, RequestBody 값 정의
        val commentJsonObject = buildJsonObject {
            put("rating", JsonPrimitive(3))
            put("movie_id", JsonPrimitive("5a54c286e8a71d136fb5378e"))
            put("timestamp", JsonPrimitive(1575813547))
            put("writer", JsonPrimitive("작성자"))
            put("contents", JsonPrimitive("꼭 보셈"))
        }
        val commentBody = """
            {
                "rating": 3,
                "movie_id": "5a54c286e8a71d136fb5378e",
                "timestamp": 1575813547,
                "writer": "작성자",
                "contents": "꼭 보셈"
            }
        """.trimIndent().toRequestBody("application/json".toMediaType())

        // WHEN 3. /comment POST 요청 시
        val postCommentResponse =
            NetworkModules.testRetrofit.postComment(data = commentBody).execute()
        val postComment = postCommentResponse.body()
        // THEN 3. 응답값이 일치해야 한다.
        Assert.assertEquals(postComment?.rating, 3)
        Assert.assertEquals(postComment?.writer, "작성자")
    }
}
