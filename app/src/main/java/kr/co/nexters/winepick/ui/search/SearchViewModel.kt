package kr.co.nexters.winepick.ui.search

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.ui.base.BaseViewModel
import timber.log.Timber

/**
 * 검색 화면 ViewModel
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.06
 */
class SearchViewModel : BaseViewModel() {
    val tag = this::class.java.canonicalName

    /** 검색 창 입력 내용 */
    val query = MutableLiveData("")

    /** filter 적용 개수 */
    private val _filterNum = MutableLiveData(1)
    val filterNum: LiveData<Int> = _filterNum

    /** 검색 결과 list */
    private val _results = MutableLiveData<List<WineResult>>(listOf())
    val results: LiveData<List<WineResult>> = _results

    /** 검색 목록 list */
    val currents: LiveData<List<String>> = SearchRepository.styledWineInfos

    /** 가장 맨 앞에 보여야 할 화면. 보여야 할 내용은 [SearchFront] 참고 */
    private val _searchFrontPage = MutableLiveData<SearchFront>(SearchFront.DEFAULT)
    val searchFrontPage: LiveData<SearchFront> = _searchFrontPage

    /**
     * 검색 화면에서 진행하는 비즈니스 로직
     * LiveData 를 사용해도 되지만 Rx 감을 찾기위해 이걸 사용했으며, 생명주기상 문제가 있을 시 [LiveData] 로 수정
     */
    private val _searchAction = PublishSubject.create<SearchAction>()
    val searchAction = _searchAction

    init {
        _searchAction.onNext(SearchAction.NONE)
    }

    override fun onResume() {
        super.onResume()
        // 도수의 경우 2개로 인식되므로 -1 처리를 해준다.
        _filterNum.value = SearchRepository.userSearchFilterItems.filter { it.selected }.size - 1
    }

    /**
     * 검색 창에 포커스가 들어올 시 실행되는 리스너
     * 텍스트 입력된 내용이 없다면 추천 레이아웃을 보여주고
     * 텍스트 입력된 내용이 있다면 최근 검색어 레이아웃을 보여준다.
     */
    val queryOneFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        when {
            // 포커스 된 상태이고 텍스트에 입력된 내용이 없으면 추천 레이아웃을 보여준다.
            hasFocus && query.value.isNullOrEmpty() -> {
                Timber.i("focusChanged $hasFocus ${SearchFront.RECOMMEND}")
                _searchFrontPage.value = SearchFront.RECOMMEND
            }
            // 포커스 된 상태이고 텍스트에 입력된 내용이 있으면 최근 검색어 레이아웃을 보여준다.
            hasFocus && query.value!!.isNotEmpty() -> {
                Timber.i("focusChanged $hasFocus ${SearchFront.CURRENT}")
                _searchFrontPage.value = SearchFront.CURRENT
            }
            // 아무 상황도 아니라면 그냥 화면을 보여준다.
            else -> {
                Timber.i("focusChanged $hasFocus ${SearchFront.DEFAULT}")
                _searchFrontPage.value = SearchFront.DEFAULT
            }
        }
    }

    /**
     * 검색창 입력 내용 textWatcher
     *
     * @see [MVVM-editText](https://steemit.com/kr/@jeonghamin/andoird-5-mvvm-edittext)
     */
    fun queryAfterTextChanged(s: Editable) {
        Timber.i("queryAfterTextChanged : s : $s")

        // 만약 Default 인 상태에서 query 의 값이 변경되었을 경우에는
        // 추천, 최근 검색어 화면이 보이지 않는 상태이므로 그대로 끝낸다.
        if (_searchFrontPage.value?.equals(SearchFront.DEFAULT) == true) return

        // 텍스트에 입력된 내용이 없으면 추천 레이아웃을 보여준다.
        if (s.isEmpty()) {
            Timber.i(tag, "${SearchFront.RECOMMEND}")
            _searchFrontPage.value = SearchFront.RECOMMEND
        } else {
            viewModelScope.launch { SearchRepository.getWineInfosLikeQuery(s.toString()) }

            Timber.i(tag, "${SearchFront.CURRENT}")
            _searchFrontPage.value = SearchFront.CURRENT

        }
    }

    /** 입력 내용 지우기 버튼을 누를 시 실행되는 로직 */
    fun queryResetClick() {
        query.value = ""
        _searchAction.onNext(SearchAction.QUERY_RESET)
    }

    /**
     * 검색 버튼 누를 시 실행되는 로직
     *
     * @param queryValue 검색할 키워드 (기본값은 query liveData 내의 value 이다.)
     */
    fun querySearchClick(queryValue: String = query.value ?: "") {
        if (!query.value.equals(queryValue)) {
            query.value = queryValue
        }

        viewModelScope.launch {
            SearchRepository.getWineInfosLikeQuery(queryValue)
            _results.value = WineRepository.getWines(10, 0)?.wineResult ?: listOf()
        }

        _searchAction.onNext(SearchAction.QUERY_SEARCH)
    }

    /** 필터 변경을 누를 경우 동작하는 로직 */
    fun filterEditClick() {
        _searchAction.onNext(SearchAction.EDIT_FILTER)
    }

    /**
     * [검색 결과 아이템 화면][R.layout.item_search_result] 에서 하트 버튼을 누를 경우 동작하는 로직
     *
     * @param wineResult 검색결과 아이템이 가지고 있는 [검색 결과 아이템 데이터][WineResult] 정보
     */
    fun searchResultHeartClick(wineResult: WineResult) {
        // TODO 좋아요 추가/취소 구현하기
    }

    override fun onCleared() {
        super.onCleared()
        SearchRepository.filterItemsClear()
    }
}

/**
 * 검색 화면에서 맨 앞에 보여주어야 할 화면 flag
 *
 * 해당 flag 가 활성화될 시, 그 flag 에 해당하는 화면을 맨 앞에 보여준다.
 */
enum class SearchFront(val value: Int) {
    // 추천 키워드 화면
    RECOMMEND(0),

    // 최근 검색어 화면
    CURRENT(1),

    // 기본 화면 (검색 결과 화면)
    DEFAULT(2),
}

/**
 * 검색 화면에서 진행하는 비즈니스 로직
 *
 * 해당 flag 가 활성화될 시, 그 flag 에 해당하는 화면을 맨 앞에 보여준다.
 */
enum class SearchAction {
    // 아무 이벤트도 없음
    NONE,

    // (필터) 변경 버튼 클릭
    EDIT_FILTER,

    // 검색창 문자열 비우기
    QUERY_RESET,

    // 검색창 문자열 기반으로 검색
    QUERY_SEARCH,
}
