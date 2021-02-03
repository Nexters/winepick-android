package kr.co.nexters.winepick.riflockle7

import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kr.co.nexters.winepick.feature.riflockle7.DummyDetailMovie
import kr.co.nexters.winepick.feature.riflockle7.DummyMovieCommentResponse
import kr.co.nexters.winepick.feature.riflockle7.DummyMovieResponse
import kr.co.nexters.winepick.feature.riflockle7.KtorModules.BASE_URL
import kr.co.nexters.winepick.feature.riflockle7.KtorModules.createHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException
import java.net.URL

/**
 * kotlinx.serializer 를 테스트
 *
 * @since v1.0.0 / 2021.02.02
 */
@RunWith(RobolectricTestRunner::class)
class KtorTest {
    companion object {
        val httpClient = createHttpClient()
    }

    @Test
    fun ktorDefaultTest() = runBlocking {
        // WHEN 1. "/movies" GET 요청 시
        val getMovies: DummyMovieResponse = httpClient.get("$BASE_URL/movies?order_type=1")
        // THEN 1. 응답값이 일치해야 한다.
        Assert.assertEquals(getMovies.movies.size, 8)
        Assert.assertEquals(getMovies.movies[0].title, "꾼")
        Assert.assertEquals(getMovies.movies[0].userRating, 5)
        Assert.assertEquals(getMovies.movies[0].reservationRate, 61.69f)
        Assert.assertEquals(getMovies.orderType, 1)

        // GIVEN 2. id 에 들어갈 파라미터 정의
        val movieId = "5a54c286e8a71d136fb5378e"
        // WHEN 2. "/movie" GET 요청 시
        val getMovie: DummyDetailMovie = httpClient.get("$BASE_URL/movie") {
            parameter("id", movieId)
        }
        // THEN 2. 응답값이 일치해야 한다.
        Assert.assertEquals(getMovie.actor, "하정우(강림), 차태현(자홍), 주지훈(해원맥), 김향기(덕춘)")
        Assert.assertEquals(
            getMovie.image,
            URL("https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/padakpadak/1.jpg")
        )
        Assert.assertEquals(getMovie.duration, 139)
        Assert.assertEquals(getMovie.reservationRate, 35.5f)

        // GIVEN 3. body json 값 정의
        val commentBody = buildJsonObject {
            put("rating", JsonPrimitive(3))
            put("movie_id", JsonPrimitive("5a54c286e8a71d136fb5378e"))
            put("timestamp", JsonPrimitive(1575813547))
            put("writer", JsonPrimitive("작성자"))
            put("contents", JsonPrimitive("꼭 보셈"))
        }
        // WHEN 3. /comment POST 요청 시
        val postComment = httpClient.post<DummyMovieCommentResponse>("$BASE_URL/comment") {
            body = commentBody
        }
        // THEN 3. 응답값이 일치해야 한다.
        Assert.assertEquals(postComment.rating, 3)
        Assert.assertEquals(postComment.writer, "작성자")
    }
}

/** [API 요청][request] 내 RequestBody 를 분석한다. 디버깅 시 활용한다. */
fun parseRequestBody(request: Request? = null, requestBody: RequestBody? = null): String? {
    return try {
        val buffer = Buffer()
        val body: RequestBody? = request?.newBuilder()?.build()?.body ?: requestBody
        body?.writeTo(buffer)
        buffer.readUtf8()
    } catch (e: IOException) {
        "분석할 수 없습니다."
    }
}
