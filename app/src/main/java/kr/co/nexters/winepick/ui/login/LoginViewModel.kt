package kr.co.nexters.winepick.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.data.model.AccessTokenData
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class LoginViewModel(private val repo : WinePickRepository, private val authManager: AuthManager) : BaseViewModel() {
    private var _login = MutableLiveData<String>()
    var login : LiveData<String> = _login
    /** 생성자 */
    init {
        _login.value = MutableLiveData<String>().value

    }

    /** 카카오 로그인 서버 통신 */
    fun addUserInfo (token : String) {
        repo.postUser(
            data = AccessTokenData(token),
            onSuccess = {
                authManager.token = it.accessToken.toString()
                authManager.autoLogin = true
                authManager.id = it.id!!
            },
            onFailure = {
            }
        )
    }


    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
