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
    /** 맨 처음 View 만들기 작업을 위해, 마지막에서 필터 변동 여부를 체크하기위해 사용하는 LiveData */
    private val _prevFilterItems = MutableLiveData<List<SearchFilterItem>>(listOf())
    val prevFilterItems: LiveData<List<SearchFilterItem>> = _prevFilterItems

    /** 값이 바뀌었는지를 확인하기 위한 list 이며, 마지막에서 필터 변동 여부 체크에서만 활용하는 변수 */
    private val _changedFilterItems = mutableListOf<SearchFilterItem>()
    private val changedFilterItems: List<SearchFilterItem> = _changedFilterItems

    /** 필터 아이템 선택으로 인해 UI 변경이 필요한 필터 아이템 */
    private val _changeSearchFilterItem = MutableLiveData<SearchFilterItem>()
    val changedFilterItem: LiveData<SearchFilterItem> = _changeSearchFilterItem

    init {
        _prevFilterItems.value = mutableListOf<SearchFilterItem>()
            .apply { addAll(SearchRepository.userSearchFilterItems) }
        _changedFilterItems.addAll(SearchRepository.userSearchFilterItems)
    }

    /** 클릭으로 인해 변경된, 필터 아이템 내용을 반영한다. */
    fun searchFilterItemClick(needToUpdateItem: SearchFilterItem) {
        Timber.i("needToUpdateItem = $needToUpdateItem")

        val prevUpdatedItem = when (needToUpdateItem.group) {
            // 중복 가능한 경우 (맛, 이벤트)
            SearchFilterGroup.TASTE, SearchFilterGroup.EVENT -> null
            // 중복 불가능한 경우
            else -> {
                // 비활성화 시켜주어야 하는 아이템 찾기 (등록한 게 없는 경우 null)
                val tempItem = prevFilterItems.value?.firstOrNull {
                    (it.group == needToUpdateItem.group) && it.selected
                }
                tempItem?.copy(selected = !tempItem.selected)
            }
        }

        val newUpdatedItem = needToUpdateItem.copy(selected = !needToUpdateItem.selected)

        if (SearchRepository.updateFilterItems(newUpdatedItem, prevUpdatedItem)) {
            logChangedFilterItems(newUpdatedItem)
            _changeSearchFilterItem.value = newUpdatedItem
            prevUpdatedItem?.let {
                logChangedFilterItems(prevUpdatedItem)
                _changeSearchFilterItem.value = prevUpdatedItem
            }
        }
    }

    /**
     * range slide 로 인해 변경된, 필터 아이템 내용을 반영한다.
     * 클릭이 아닌 즉시 수정이 필요한 아이템에는 이 로직을 사용한다.
     */
    fun replaceFilterItem(needToReplaceItem: SearchFilterItem, newSliderValue: String) {
        Timber.i("needToUpdateItem = $needToReplaceItem")

        val newUpdatedItem = needToReplaceItem.copy(value = newSliderValue)

        if (SearchRepository.updateFilterItems(newUpdatedItem)) {
            logChangedFilterItems(newUpdatedItem)
            _changeSearchFilterItem.value = newUpdatedItem
        }
    }

    /** [changedFilterItems] 에 변경된 내역을 로깅한다. */
    private fun logChangedFilterItems(newUpdatedItem: SearchFilterItem) {
        _changedFilterItems.removeAt(newUpdatedItem.id)
        _changedFilterItems.add(newUpdatedItem.id, newUpdatedItem)
    }

    /**
     * [prevFilterItems] 와 [changedFilterItems] 을 비교하여 변경된 내용이 있는지 확인한다.
     *
     * 변동이 없다면 [SearchRepository.userSearchFilterItems] 도 롤백시키는 로직도 있기 때문에
     * 반드시 마지막 [SearchFilterActivity.onBackPressedAtSearchFilter] 에서만 이 로직을 실행한다.
     *
     * @param needToCheck 변경 여부를 확인해야 하는지에 대한 flag
     *
     * @return [SearchActivity] 에서 검색 목록 갱신 작업을 해야하는지에 대한 flag
     */
    fun checkFilterItemChanged(needToCheck: Boolean): Boolean {
        val needToUpdate = false

        if (needToCheck)
            prevFilterItems.value!!.zip(changedFilterItems) { prev, changed ->
                if (prev != changed) {
                    return true
                }
            }

        // 여기까지 왔으면 변경된 내용이 없거나, 변경된 내역을 기억하면 안되는 것이므로
        // SearchRepository 에 저장되어 있는 내용도 롤백시킨다.
        // (prevFilterItems 은 null 일 수가 없다.)
        SearchRepository.rollbackFilterItems(prevFilterItems.value!!)

        return needToUpdate
    }
}