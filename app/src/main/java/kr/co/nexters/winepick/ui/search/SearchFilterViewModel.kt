package kr.co.nexters.winepick.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.data.model.local.SearchFilterGroup
import kr.co.nexters.winepick.data.model.local.SearchFilterItem
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.ui.base.BaseViewModel
import timber.log.Timber

/**
 * 검색 화면 ViewModel
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.06
 */
class SearchFilterViewModel : BaseViewModel() {
    /** 단순히 맨 처음 View 만들기 작업을 위해 존재하는 LiveData 이며 사용할 일 없는 변수 */
    private val _searchFilterItems = MutableLiveData<List<SearchFilterItem>>(listOf())
    val searchFilterItems: LiveData<List<SearchFilterItem>> = _searchFilterItems

    /** 필터 아이템 선택으로 인해 selected 가 바뀌어야 하는 필터 아이템 */
    val _changeSearchFilterItem = MutableLiveData<SearchFilterItem>()
    val changeSearchFilterItem: LiveData<SearchFilterItem> = _changeSearchFilterItem

    init {
        _searchFilterItems.value = SearchRepository.userSearchFilterItems
    }

    fun searchFilterItemClick(needToUpdateItem: SearchFilterItem) {
        Timber.i("needToUpdateItem = $needToUpdateItem")

        val prevUpdatedItem = when (needToUpdateItem.group) {
            // 중복 가능한 경우 (맛, 이벤트)
            SearchFilterGroup.TASTE, SearchFilterGroup.EVENT -> null
            // 중복 불가능한 경우
            else -> {
                // 비활성화 시켜주어야 하는 아이템 찾기 (등록한 게 없는 경우 null)
                val tempItem = searchFilterItems.value?.firstOrNull {
                    (it.group == needToUpdateItem.group) && it.selected
                }
                tempItem?.copy(selected = !tempItem.selected)
            }
        }

        val newUpdateItem = needToUpdateItem.copy(selected = !needToUpdateItem.selected)

        if (SearchRepository.updateFilterItems(newUpdateItem, prevUpdatedItem)) {
            _changeSearchFilterItem.value = newUpdateItem
            prevUpdatedItem?.let { _changeSearchFilterItem.value = prevUpdatedItem }
        }
    }

    fun replaceFilterItem(needToReplaceItem: SearchFilterItem, newSliderValue : String) {
        Timber.i("needToUpdateItem = $needToReplaceItem")

        val newUpdatedItem = needToReplaceItem.copy(value = newSliderValue)

        if(SearchRepository.updateFilterItems(newUpdatedItem)){
            _changeSearchFilterItem.value = newUpdatedItem
        }
    }
}
