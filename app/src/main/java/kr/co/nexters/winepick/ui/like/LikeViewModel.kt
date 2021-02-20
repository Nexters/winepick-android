package kr.co.nexters.winepick.ui.like

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class LikeViewModel(private val repo : WinePickRepository, private val auth: AuthManager) : BaseViewModel() {
    private var _like_num = MutableLiveData<Int>()

    var likeNum : LiveData<Int> = _like_num

    private val _wineList : MutableLiveData<List<WineResult>?> = MutableLiveData()
    val wineList : LiveData<List<WineResult>?> = _wineList


    /** 생성자 */
    init {
        _like_num.value = 0

    }

    /** 제목을 변경한다. UI 에서 [_title] 에 직접 접근하는 것을 막기 위해 사용한다. */
    fun editLikeNum(num: Int) {
        _like_num.value = num
    }

    fun getLikeWine() {
        repo.getLikeWineList(
            userId = auth.id,
            onSuccess = {
                _wineList.value = it
                _like_num.value = it.size
            },
            onFailure = {
                _wineList.value = null
            }
        )
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
