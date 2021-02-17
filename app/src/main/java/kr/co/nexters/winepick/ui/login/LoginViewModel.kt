package kr.co.nexters.winepick.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.model.UserData
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.data.response.UserInfoData
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.util.onErrorStub
import org.koin.android.ext.android.inject
import org.koin.experimental.property.inject
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class LoginViewModel(private val repo : WinePickRepository) : BaseViewModel() {
    private var _login = MutableLiveData<String>()
    var login : LiveData<String> = _login
    /** 생성자 */
    init {
        _login.value = MutableLiveData<String>().value

    }

    /** 제목을 변경한다. UI 에서 [_title] 에 직접 접근하는 것을 막기 위해 사용한다. */
    fun editTitle(title: String) {
        _login.value = title
    }

    fun addUserInfo (token : String) {
        repo.postUser(
            data = UserData(token),
            onSuccess = {
                        Timber.e("ddd?")
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
