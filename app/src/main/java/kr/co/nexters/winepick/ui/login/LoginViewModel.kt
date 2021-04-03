package kr.co.nexters.winepick.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.AccessTokenData
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeActivity

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
    fun addUserInfo (token : String, personalityType: String?, userId : Long) {
        repo.postUser(
            data = AccessTokenData(token, personalityType!!, userId!!),
            onSuccess = {
                authManager.token = it.accessToken.toString()
                authManager.autoLogin = true
                authManager.id = it.id!!
                authManager.testType = it.personalityType!!
                Intent(WinePickApplication.appContext, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    WinePickApplication.getGlobalApplicationContext().startActivity(this)
                }
            },
            onFailure = {
                authManager.testType = "N"
                _toastMeesageText.value = WinePickApplication.getGlobalApplicationContext()
                    .resources.getString(R.string.api_error)
            }
        )
    }


    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }
}
