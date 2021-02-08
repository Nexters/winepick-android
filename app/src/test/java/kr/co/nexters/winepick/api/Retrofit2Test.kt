package kr.co.nexters.winepick.api

import androidx.test.core.app.ApplicationProvider
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.DummyDetailMovie
import kr.co.nexters.winepick.data.repository.TestRepository
import kr.co.nexters.winepick.data.response.DummyMovieCommentResponse
import kr.co.nexters.winepick.data.response.DummyMovieResponse
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
    fun retrofit2DefaultTest() {
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

    @Test
    fun retrofit2CoroutineTest() = runBlocking {
        try {
            // WHEN 1. "/movies" GET 요청 시
            val getMovies = TestRepository.getMovies(1)

            // THEN 1. 응답값이 일치해야 한다.
            Assert.assertEquals(getMovies?.movies?.size, 8)
            Assert.assertEquals(getMovies?.movies?.get(0)?.title, "꾼")
            Assert.assertEquals(getMovies?.movies?.get(0)?.userRating, 5)
            Assert.assertEquals(getMovies?.movies?.get(0)?.reservationRate, 61.69f)
            Assert.assertEquals(getMovies?.orderType, 1)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

        try {
            // GIVEN 2. id 에 들어갈 파라미터 정의
            val movieId = "5a54c286e8a71d136fb5378e"

            // WHEN 2. "/movie" GET 요청 시
            val getMovie = TestRepository.getMovie(movieId)

            // THEN 2. 응답값이 일치해야 한다.
            Assert.assertEquals(getMovie?.actor, "하정우(강림), 차태현(자홍), 주지훈(해원맥), 김향기(덕춘)")
            Assert.assertEquals(
                getMovie?.image,
                URL("https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/padakpadak/1.jpg")
            )
            Assert.assertEquals(getMovie?.duration, 139)
            Assert.assertEquals(getMovie?.reservationRate, 35.5f)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }

        try {
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
            val postComment = TestRepository.postComment(body = commentBody)

            // THEN 3. 응답값이 일치해야 한다.
            Assert.assertEquals(postComment?.rating, 3)
            Assert.assertEquals(postComment?.writer, "작성자")
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    @Test
    fun retrofit2RxTest() {
        // WHEN 1. "/movies" GET 요청 시
        TestRepository.getMoviesRx(1)
            .doOnSuccess { println("doOnSuccess") }
            .subscribeWith(object :
                DisposableSingleObserver<DummyMovieResponse>() {
                override fun onSuccess(response: DummyMovieResponse?) {
                    // THEN 1. 응답값이 일치해야 한다.
                    Assert.assertEquals(response?.movies?.size, 8)
                    Assert.assertEquals(response?.movies?.get(0)?.title, "꾼")
                    Assert.assertEquals(response?.movies?.get(0)?.userRating, 5)
                    Assert.assertEquals(response?.movies?.get(0)?.reservationRate, 61.69f)
                    Assert.assertEquals(response?.orderType, 1)
                }

                override fun onError(throwable: Throwable) {
                    throwable.printStackTrace()
                }
            })

        // GIVEN 2. id 에 들어갈 파라미터 정의
        val movieId = "5a54c286e8a71d136fb5378e"
        // WHEN 2. "/movie" GET 요청 시
        TestRepository.getMovieRx(movieId)
            .doOnSuccess { println("doOnSuccess") }
            .subscribeWith(object :
                DisposableSingleObserver<DummyDetailMovie>() {
                override fun onSuccess(response: DummyDetailMovie?) {
                    // THEN 2. 응답값이 일치해야 한다.
                    Assert.assertEquals(response?.actor, "하정우(강림), 차태현(자홍), 주지훈(해원맥), 김향기(덕춘)")
                    Assert.assertEquals(
                        response?.image,
                        URL("https://raw.githubusercontent.com/riflockle7/ref/master/1.%20ImageRef/padakpadak/1.jpg")
                    )
                    Assert.assertEquals(response?.duration, 139)
                    Assert.assertEquals(response?.reservationRate, 35.5f)
                }

                override fun onError(throwable: Throwable) {
                    throwable.printStackTrace()
                }
            })

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
        TestRepository.postCommentRx(body = commentBody)
            .doOnSuccess { println("doOnSuccess") }
            .subscribeWith(object :
                DisposableSingleObserver<DummyMovieCommentResponse>() {
                override fun onSuccess(response: DummyMovieCommentResponse?) {
                    // THEN 3. 응답값이 일치해야 한다.
                    Assert.assertEquals(response?.rating, 3)
                    Assert.assertEquals(response?.writer, "작성자")
                }

                override fun onError(throwable: Throwable) {
                    throwable.printStackTrace()
                }
            })
    }

}
