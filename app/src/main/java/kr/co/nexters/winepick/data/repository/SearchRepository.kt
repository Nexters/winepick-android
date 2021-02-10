package kr.co.nexters.winepick.data.repository

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.DummyDetailMovie
import kr.co.nexters.winepick.data.response.DummyMovieCommentResponse
import kr.co.nexters.winepick.data.response.DummyMovieResponse
import kr.co.nexters.winepick.data.source.SearchDataSource
import kr.co.nexters.winepick.network.NetworkModules
import kr.co.nexters.winepick.network.NetworkModules.send
import okhttp3.RequestBody

/**
 * 테스트 Repository
 *
 * @since v1.0.0 / 2021.02.04
 */
object SearchRepository {
    suspend fun getSearchResults(query: String, page: Int) = withContext(Dispatchers.IO) {
        return@withContext SearchDataSource.getSearchResults(query, page)
    }
}
