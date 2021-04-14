package kr.co.nexters.winepick.ui.search

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.local.LastPageException
import kr.co.nexters.winepick.data.model.local.SearchFilterGroup
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import timber.log.Timber
import javax.inject.Inject

/**
 * 검색 화면 ViewModel
 *
 * @since v1.0.0 / 2021.02.06
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository, private val wineRepository: WineRepository,
    private val winePickRepository: WinePickRepository, private val authManager: AuthManager
) : WineResultViewModel(winePickRepository) {
    val tag = this::class.java.canonicalName

    /** 검색 창 입력 내용 */
    val query = MutableLiveData("")

    /** filter 적용 개수 */
    private val _filterNum = MutableLiveData(1)
    val filterNum: LiveData<Int> = _filterNum

    /** 검색 목록 list */
    val currents: LiveData<List<String>> = searchRepository.styledWineInfos

    /** 현재 클릭한 와인 검색결과 위치 */
    val _clickedWineResult: MutableLiveData<WineResult> = MutableLiveData()
    val clickedWineResult: LiveData<WineResult> = _clickedWineResult

    /** 가장 맨 앞에 보여야 할 화면. 보여야 할 내용은 [SearchFront] 참고 */
    private val _searchFrontPage = MutableLiveData(SearchFront.DEFAULT)
    val searchFrontPage: LiveData<SearchFront> = _searchFrontPage

    private val _initAction = PublishSubject.create<SearchType>()
    val initAction = _initAction

    private var page: Int = 0

    /** 좋아요 토스트 **/
    val _toastMessage = MutableLiveData<Boolean>()
    var toastMessage: LiveData<Boolean> = _toastMessage

    /** 취소 토스트 **/
    val _cancelMessage = MutableLiveData<Boolean>()
    var cancelMessage: LiveData<Boolean> = _cancelMessage

    /**
     * 검색 화면에서 진행하는 비즈니스 로직
     * LiveData 를 사용해도 되지만 Rx 감을 찾기위해 이걸 사용했으며, 생명주기상 문제가 있을 시 [LiveData] 로 수정
     */
    private val _searchAction = PublishSubject.create<SearchAction>()
    val searchAction = _searchAction

    /**
     * 현재 검색화면에서 보여주고 있는 리스트 내용의 타입
     */
    private val _searchResultType = MutableLiveData<SearchType>()
    val searchResultType: LiveData<SearchType> = _searchResultType

    private var _loginWarningDlg : MutableLiveData<Boolean> = MutableLiveData()
    val loginWarningDlg : LiveData<Boolean> = _loginWarningDlg

    init {
        _searchAction.onNext(SearchAction.NONE)
    }

    override fun onResume() {
        super.onResume()
        // 도수의 경우 2개로 인식되므로 -1 처리를 해준다.
        _filterNum.value = searchRepository.userSearchFilterItems.filter { it.selected }.size - 1
    }

    fun initEmptyAction(){
        _searchFrontPage.value = SearchFront.RECOMMEND
    }

    fun prevDataClear() {
        _results.value = listOf()
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
            viewModelScope.launch { searchRepository.getWineInfosLikeQuery(s.toString()) }

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
     * @param recommendValue 검색할 키워드 (기본값은 query liveData 내의 value 이다.)
     */
    fun queryRecommendClick(recommendValue: String = "", page: Int) {
        _searchResultType.value = SearchType.RECOMMEND

        if(page == 0)
            _initAction.onNext(SearchType.RECOMMEND)

        if (!query.value.equals(recommendValue)) {
            query.value = recommendValue
        }

        viewModelScope.launch {
            searchRepository.getWineInfosLikeQuery(recommendValue)

            this@SearchViewModel.page = page

            _results.value = try {
                wineRepository.getWinesKeyword(
                    size = 10,
                    page = page,
                    keyword = recommendValue
                )?.wineResult ?: listOf()
            } catch (throwable: Throwable) {
                listOf()
            }
        }

        _searchAction.onNext(SearchAction.QUERY_SEARCH)
    }

    /**
     * 검색 버튼 누를 시 실행되는 로직
     *
     * @param queryValue 검색할 키워드 (기본값은 query liveData 내의 value 이다.)
     */
    fun querySearchClick(queryValue: String = query.value ?: "", page: Int) {
        _searchResultType.value = SearchType.DEFAULT

        if(page == 0)
            _initAction.onNext(SearchType.DEFAULT)

        if (!query.value.equals(queryValue)) {
            query.value = queryValue
        }

        viewModelScope.launch {
            searchRepository.getWineInfosLikeQuery(queryValue)

            this@SearchViewModel.page = page

            val contents =
                searchRepository.getSearchFilters<Pair<String, String>>(SearchFilterGroup.CONTENT)
            val type = searchRepository.getSearchFilters<String>(SearchFilterGroup.TYPE)
            val food = searchRepository.getSearchFilters<String>(SearchFilterGroup.FOOD)
            val store = searchRepository.getSearchFilters<String>(SearchFilterGroup.CONVENIENCE)
            val tastes = searchRepository.getSearchFilters<List<String>>(SearchFilterGroup.TASTE)
            val events = searchRepository.getSearchFilters<List<String>>(SearchFilterGroup.EVENT)
            val keywords = mutableListOf("").apply {
                tastes?.let { addAll(it) }
                events?.let { addAll(it) }
            }

            val newResults = try {
                wineRepository.getWinesFilter(
                    wineName = queryValue,
                    category = type,
                    food = food,
                    store = store,
                    start = contents?.first?.toInt(),
                    end = contents?.second?.toInt(),
                    keywords = keywords,
                    size = 10,
                    page = page
                )?.wineResult ?: listOf()
            } catch (throwable: Throwable) {
                when (throwable) {
                    is LastPageException -> Timber.i("마지막 페이지입니다.")
                }

                listOf()
            }

            _results.value = results.value?.toMutableList()
                ?.apply { addAll(newResults) } ?: listOf()
        }

        _searchAction.onNext(SearchAction.QUERY_SEARCH)
    }

    /**
     * query 내용을 기반으로 페이징 처리를 진행한다.
     * 현재 리스트의 타입 내용[SearchType]에 따라 호출하는 로직이 다르다.
     */
    fun paging() {
        page++
        when (searchResultType.value) {
            SearchType.DEFAULT -> {
                querySearchClick(query.value ?: "", page)
            }
            SearchType.RECOMMEND -> {
                queryRecommendClick(query.value ?: "", page)
            }
            else -> {
                querySearchClick(query.value ?: "", page)
            }
        }
    }

    /** 필터 변경을 누를 경우 동작하는 로직 */
    fun filterEditClick() {
        _searchAction.onNext(SearchAction.EDIT_FILTER)
    }

    /** 해당 position 을 업데이트한다. 값 변경이 있을 시 해당 내용을 반영한다. */
    fun updateClickPosition(wineResult: WineResult) = viewModelScope.launch(vmExceptionHandler) {
        val index = results.value?.indexOf(wineResult) ?: return@launch

        val updatedWineResult = wineRepository.getWine(wineResult.id ?: return@launch)

        val prevWineResult = _results.value?.toMutableList()?.apply {
            removeAt(index)
            add(index, updatedWineResult!!)
        }

        _results.value = prevWineResult
    }

    override fun wineItemViewClick(wineResult: WineResult) {
        Timber.i("wineItemViewClick search $wineResult")
        _clickedWineResult.value = wineResult
    }

    override fun wineHeartClick(newWineResult: WineResult) {
        Timber.i("wineHeartClick search $newWineResult")
        if (authManager.token != "guest") {
            showLoading()
            if (newWineResult.clickedLikeYn) {
                cancelLike(authManager.id, newWineResult)
            } else {
                addLike(newWineResult)
            }
        } else {
            _loginWarningDlg.value = true
        }
    }

    /** 좋아요 서버 통신 - addLike */
    fun addLike(wineResult: WineResult) {
        winePickRepository.postLike(
            data = LikeWine(
                userId = authManager.id,
                wineId = wineResult.id!!
            ),
            onSuccess = {
                Timber.d("와인 좋아요 저장 성공")
                _toastMessage.value = true
                hideLoading()
            },
            onFailure = {
                toggleWineResult(wineResult)
                hideLoading()
            }
        )

    }

    /** 좋아요 취소 서버 통신 - deleteLike */
    fun cancelLike(userId: Int, wineResult: WineResult) {
        winePickRepository.deleteLike(
            wineId = wineResult.id!!,
            userId = userId,
            onSuccess = {
                Timber.d("와인 좋아요 취소 성공")
                _cancelMessage.value = true
                hideLoading()
            },
            onFailure = {
                toggleWineResult(wineResult)
                hideLoading()
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        page = 0
        searchRepository.filterItemsClear()
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

/** 현재 검색 화면에서 보여주고 있는 리스트의 타입 */
enum class SearchType {
    // 필터링 및 와인명 검색을 통해 보여주는 타입
    DEFAULT,

    // 빠른 키워드 검색을 통해 보여주는 타입
    RECOMMEND
}
