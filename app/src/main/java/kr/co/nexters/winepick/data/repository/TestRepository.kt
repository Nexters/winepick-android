package kr.co.nexters.winepick.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.DummyDetailMovie
import kr.co.nexters.winepick.data.response.DummyMovieCommentResponse
import kr.co.nexters.winepick.data.response.DummyMovieResponse
import kr.co.nexters.winepick.di.send
import kr.co.nexters.winepick.network.TestService
import okhttp3.RequestBody

/**
 * 테스트 Repository
 *
 * @since v1.0.0 / 2021.02.04
 */
class TestRepository(private val testService: TestService) {
    suspend fun getMovies(orderType: Int) = withContext(Dispatchers.IO) {
        // dataSource 가 필요한 경우 옮긴다.
        val response = testService.getMovies(orderType).send()

        /** statusCode 별 처리 */
        when (response.code()) {

        }

        if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

        return@withContext response.body()
    }

    fun getMoviesRx(orderType: Int): Single<DummyMovieResponse> {
        return testService.getMoviesRx(orderType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    suspend fun getMovie(id: String) = withContext(Dispatchers.IO) {
        val response = testService.getMovie(id).send()

        /** statusCode 별 처리 */
        when (response.code()) {

        }

        if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

        return@withContext response.body()
    }

    fun getMovieRx(id: String): Single<DummyDetailMovie> {
        return testService.getMovieRx(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    suspend fun postComment(body: RequestBody) = withContext(Dispatchers.IO) {
        val response = testService.postComment(body).send()

        /** statusCode 별 처리 */
        when (response.code()) {

        }

        if (!response.isSuccessful) throw Throwable("${response.code()} API 오류")

        return@withContext response.body()
    }

    fun postCommentRx(body: RequestBody): Single<DummyMovieCommentResponse> {
        return testService.postCommentRx(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
