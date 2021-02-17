package kr.co.nexters.winepick.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class HomeViewModel(private val repo : WinePickRepository, private val authManager : AuthManager) : BaseViewModel() {
    private var _home = MutableLiveData<String>()
    var home : LiveData<String> = _home
    private var _likecnt = MutableLiveData<Int>()
    var likeCnt : LiveData<Int> = _likecnt

    private var _checkTest : MutableLiveData<Boolean> = MutableLiveData()
    var checkTest : LiveData<Boolean> = _checkTest


    /** 생성자 */
    init {
        _home.value = MutableLiveData<String>().value
        _likecnt.value = 0

        if (!authManager.test_type.isNullOrEmpty()) {
            _checkTest.value = true
        }

    }


    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
