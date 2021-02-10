package kr.co.nexters.winepick.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kr.co.nexters.winepick.MainActivity
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityLoginBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.util.startActivity
import timber.log.Timber

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    R.layout.activity_login
) {
    override val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    val sessionCallback: ISessionCallback = object : ISessionCallback {
        override fun onSessionOpened() {
            Timber.e("로그인 성공")
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    Timber.e("로그인 성공")
                    Timber.e("${result!!.kakaoAccount.birthday}")
                    Timber.e("${result!!.kakaoAccount.profile.nickname}")
                    startActivity(MainActivity::class)

                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                }

            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            Timber.e("로그인 실패 - ${exception}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Session.getCurrentSession().addCallback(sessionCallback)
        binding.setVariable(BR.vm, viewModel)
        binding.apply {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)) {
            Timber.e("현재 세션")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}