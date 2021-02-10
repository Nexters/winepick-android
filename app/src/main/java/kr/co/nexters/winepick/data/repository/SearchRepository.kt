package kr.co.nexters.winepick.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.local.SearchCurrent
import kr.co.nexters.winepick.data.source.SearchDataSource
import kr.co.nexters.winepick.ui.search.SearchActivity

/**
 * 검색화면[SearchActivity] 관련 Repository
 *
 * @since v1.0.0 / 2021.02.10
 */
object SearchRepository {
    /** 원본 검색 목록 */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var searchCurrents: List<SearchCurrent> = SearchDataSource.getSearchCurrents()

    /**
     * 스타일링된 검색 목록
     *
     * 최대 10개까지만 보여주며
     * 매치되는 문자열들을 bold 처리하여 보여준다.
     */
    private val _styledSearchCurrents = MutableLiveData<List<SearchCurrent>>(listOf())
    val styledSearchCurrents: LiveData<List<SearchCurrent>> = _styledSearchCurrents

    /**
     * 스타일링처리되지 않은 [searchCurrent] 를 받고 내부 DB 에 저장한다.
     * 저장한 이후 스타일링된 문자열이 아닌 검색 목록을 [_searchCurrents] 에 넣는다.
     */
    suspend fun addSearchCurrent(
        searchCurrent: SearchCurrent, query: String? = null
    ) = withContext(Dispatchers.IO) {
        // 원본 검색 목록 업데이트
        searchCurrents = SearchDataSource.addSearchCurrent(searchCurrents, searchCurrent)

        if (!query.isNullOrEmpty()) stylingSearchCurrent(query)
    }

    suspend fun stylingSearchCurrent(query: String) = withContext(Dispatchers.IO) {
        _styledSearchCurrents.postValue(searchCurrents.filter { it.value.startsWith(query) }
            .map { it.copy(value = "<b>$query</b>${it.value.split(query).last()}") }
        )
    }
}
