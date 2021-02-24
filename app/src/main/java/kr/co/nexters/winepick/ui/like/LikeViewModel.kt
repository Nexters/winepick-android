package kr.co.nexters.winepick.ui.like

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.getWinesResponse
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class LikeViewModel(private val repo: WinePickRepository, private val auth: AuthManager) :
    WineResultViewModel() {
    private var _like_num = MutableLiveData<Int>()
    var likeNum: LiveData<Int> = _like_num

    private var _backButton = MutableLiveData<Boolean>()
    var backButton : LiveData<Boolean> = _backButton

    /** 생성자 */
    init {
        _like_num.value = 0
        _backButton.value = false
        getLikeWine()
    }

    override fun wineItemViewClick(wineResult: WineResult) {
        // TODO. 좋아요 목록 화면에서 아이템 클릭 구현
        Timber.i("wineItemViewClick like $wineResult")
    }

    override fun wineHeartClick(wineResult: WineResult) {
        // TODO. 좋아요 목록 화면에서 좋아요 / 좋아요 취소 클릭 구현
        Timber.i("wineHeartClick like $wineResult")
    }

    /** 제목을 변경한다. UI 에서 [_title] 에 직접 접근하는 것을 막기 위해 사용한다. */
    fun editLikeNum(num: Int) {
        _like_num.value = num
    }

    fun getLikeWine() {
        repo.getLikeWineList(
            userId = auth.id,
            onSuccess = {
                _results.value = it
                _like_num.value = it.size
            },
            onFailure = {
                _results.value = null
            }
        )
    }
    fun backClick() {
        _backButton.value = true
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
