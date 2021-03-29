package kr.co.nexters.winepick.ui.like

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.WineResultViewModel
import kr.co.nexters.winepick.ui.detail.WineDetailActivity
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
    }

    override fun wineItemViewClick(wineResult: WineResult) {
        Timber.i("wineItemViewClick like $wineResult")
        Intent(WinePickApplication.appContext, WineDetailActivity::class.java).apply {
            putExtra("wineData",wineResult)
        }.run {
            WinePickApplication.getGlobalApplicationContext().startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun wineHeartClick(newWineResult: WineResult) {
        showLoading()
        Timber.i("wineHeartClick search $newWineResult")
        Timber.e("${newWineResult.likeYn}")
        winePickRepository.deleteLike(
            wineId = newWineResult.id!!,
            userId = authManager.id,
            onSuccess = {
                deleteWineResult(newWineResult)
                _cancelMessage.value = true
                editLikeNum(num = _like_num.value!! -1)
                hideLoading()
            },
            onFailure = {
                toggleWineResult(newWineResult)
                hideLoading()
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
                hideLoading()
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
