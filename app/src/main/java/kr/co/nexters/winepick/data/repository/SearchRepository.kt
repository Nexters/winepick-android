package kr.co.nexters.winepick.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.local.SearchFilterItem
import kr.co.nexters.winepick.data.source.SearchDataSource
import kr.co.nexters.winepick.ui.search.SearchActivity
import java.util.*

/**
 * 검색화면[SearchActivity] 관련 Repository
 *
 * @since v1.0.0 / 2021.02.10
 */
object SearchRepository {
    /** 원본 와인 목록 */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val wineInfos: List<String> = SearchDataSource.getWineInfos()

    /** 초기 필터 목록 */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val searchFilterItems: List<SearchFilterItem> = SearchDataSource.getSearchFilterItems()

    /**
     * 스타일링된 검색 목록
     *
     * 최대 10개까지만 보여주며
     * 매치되는 문자열들을 bold 처리하여 보여준다.
     */
    private val _styledWineInfos = MutableLiveData<List<String>>(listOf())
    val styledWineInfos: LiveData<List<String>> = _styledWineInfos

    /** 유저가 필터 설정한 내역 */
    private val _userSearchFilterItems = searchFilterItems.toMutableList()
    val userSearchFilterItems: List<SearchFilterItem> = _userSearchFilterItems


    /**
     * 특정 query 에 일치하는 와인 목록을 찾는다.
     * 찾은 후 그 값들에서 query 와 일치하는 부분을 스타일링 한다.
     *
     * @return 필터링된 와인 정보 list (테스트 내용을 확인하기 위한 리턴 값)
     *
     * @see styledWineInfos
     */
    suspend fun getWineInfosLikeQuery(
        query: String? = null
    ): List<String> = withContext(Dispatchers.IO) {
        val filteredWineInfos = wineInfos.filter {
            it.toLowerCase(Locale.ROOT).startsWith(query?.toLowerCase(Locale.ROOT) ?: "")
        }

        if (!query.isNullOrEmpty()) stylingWineInfos(filteredWineInfos, query)

        return@withContext filteredWineInfos
    }

    /**
     * 필터링하여 얻어낸 문자열에서 [query] 와 일치하는 부분을 스타일링 (굵음 처리) 한다.
     *
     * @return [query] 부분이 스타일링 처리된 와인 정보 list (테스트 내용을 확인하기 위한 리턴 값)
     */
    suspend fun stylingWineInfos(
        filteredWineInfos: List<String>,
        query: String
    ): List<String> = withContext(Dispatchers.IO) {
        val styledWineInfos = filteredWineInfos.map {
            "<b>${it.substring(0, query.length)}</b>${it.substring(query.length)}"
        }

        _styledWineInfos.postValue(styledWineInfos)

        return@withContext styledWineInfos
    }

    /**
     * [newUpdateItem], [prevUpdatedItem] 와 매치되는 정보를 변경한다.
     *
     * @param newUpdateItem 새로 업데이트 해줘야 하는 item
     * @param prevUpdatedItem 중복 선택이 안되는 아이템인 경우 롤백 시켜야 하는 item
     *
     * @return 성공했는지 유무
     */
    fun updateFilterItems(
        newUpdateItem: SearchFilterItem,
        prevUpdatedItem: SearchFilterItem? = null
    ): Boolean {
        val newList = mutableListOf<SearchFilterItem>().apply {
            addAll(
                _userSearchFilterItems.apply {
                    // newUpdateItem 처리
                    removeAt(newUpdateItem.id)
                    add(newUpdateItem.id, newUpdateItem)

                    // prevUpdatedItem 처리
                    prevUpdatedItem?.let {
                        removeAt(it.id)
                        add(it.id, it)
                    }
                }
            )
        }

        _userSearchFilterItems.clear()
        _userSearchFilterItems.addAll(newList)

        return true
    }

    /** 필터 적용되었던 내용을 원상태로 복구시킨다. */
    fun filterItemsClear() {
        _userSearchFilterItems.clear()
        _userSearchFilterItems.addAll(searchFilterItems)
    }
}
