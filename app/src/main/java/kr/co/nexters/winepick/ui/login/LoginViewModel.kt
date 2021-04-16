package kr.co.nexters.winepick.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * LoginViewModel
 *
 * @since v1.0.0 / 2021.02.08
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
