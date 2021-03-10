package kr.co.nexters.winepick.ui.like

import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.LikeWine
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.getWinesResponse
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import kr.co.nexters.winepick.ui.search.WineResultAdapter
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class LikeViewModel(private val winePickRepository: WinePickRepository, private val authManager: AuthManager) :
    WineResultViewModel() {
    private var _like_num = MutableLiveData<Int>()
    var likeNum: LiveData<Int> = _like_num

    private var _backButton = MutableLiveData<Boolean>()
    var backButton : LiveData<Boolean> = _backButton

    /** 취소 토스트 **/
    val _cancelMessage = MutableLiveData<Boolean>()
    var cancelMessage : LiveData<Boolean> = _cancelMessage


    /** 생성자 */
    init {
        showLoading()
        _like_num.value = 0
        _backButton.value = false
        getLikeWine()
        hideLoading()
    }

    override fun wineItemViewClick(wineResult: WineResult) {
        Timber.i("wineItemViewClick like $wineResult")
    }

    override fun wineHeartClick(wineResult: WineResult) {
        Timber.i("wineHeartClick search $wineResult")
        Timber.e("${wineResult.likeYn}")
        winePickRepository.deleteLike(
            wineId = wineResult.id!!,
            userId = authManager.id,
            onSuccess = {
                deleteWineResult(wineResult)
                _cancelMessage.value = true
                editLikeNum(num = _like_num.value!! -1)
            },
            onFailure = {

            }
        )

    }

    fun editLikeNum(num: Int) {
        _like_num.value = num
    }

    fun getLikeWine() {
        showLoading()
        winePickRepository.getLikeWineList(
            userId = authManager.id,
            onSuccess = {
                _results.value = it
                _like_num.value = it.size
                hideLoading()

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
