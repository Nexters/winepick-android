package kr.co.nexters.winepick.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.ui.base.BaseViewModel

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class LoginViewModel : BaseViewModel() {
    private var _login = MutableLiveData<String>()
    private var _guest = MutableLiveData<String>()

    var login : LiveData<String> = _login
    var guest : LiveData<String> = _guest

    /** 생성자 */
    init {
        _login.value = "카카오 로그인 하기"
        _guest.value = "먼저 둘러보고 싶어요."
    }

    /** 제목을 변경한다. UI 에서 [_title] 에 직접 접근하는 것을 막기 위해 사용한다. */
    fun editTitle(title: String) {
        _login.value = title
    }

    fun editGuestTitle(title: String) {
        _guest.value = title
    }

    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
