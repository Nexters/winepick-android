package kr.co.nexters.winepick.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.AccessTokenData
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeActivity
import javax.inject.Inject

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    winePickRepository: WinePickRepository
) : BaseViewModel(winePickRepository) {
    private var _login = MutableLiveData<String>()
    var login: LiveData<String> = _login

    /** 생성자 */
    init {
        _login.value = MutableLiveData<String>().value
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
