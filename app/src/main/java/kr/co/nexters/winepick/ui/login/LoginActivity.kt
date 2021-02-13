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
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.inject
import org.koin.experimental.property.inject
import timber.log.Timber

class LoginActivity : BaseActivity<ActivityLoginBinding>(
    R.layout.activity_login
) {
    override val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private val authManager : AuthManager by inject()

    val sessionCallback: ISessionCallback = object : ISessionCallback {
        override fun onSessionOpened() {
            Timber.d("로그인 성공")
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    Timber.d("로그인 성공")
                    Timber.d("${result!!.kakaoAccount.birthday}")
                    Timber.d("${result!!.kakaoAccount.profile.nickname}")
                    authManager.apply {
                        this.token = result!!.kakaoAccount.profile.nickname
                    }
                    Timber.e("로그인 ${authManager.token}")
                    Intent(applicationContext,HomeActivity::class.java).apply {
                        putExtra("mode","user")
                    }.run { startActivity(this) }
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Timber.e("SessionClose - ${errorResult}")
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
            tvGuest.text = "먼저 둘러보고 싶어요."
            tvGuest.setOnClickListener {
                Intent(applicationContext,HomeActivity::class.java).apply {
                    putExtra("mode","guest")
                }.run { startActivity(this) }
            }

            // TODO 검색화면을 볼 수 있도록 하기 위해 임시 구현된 로직 (배포 시에는 삭제한다)
            var count = 0
            tvAppName.setOnClickListener {
                if (count++ > 10) {
                    startActivity(SearchActivity::class, false, null)
                    count = 0
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)) {
            Timber.d("현재 세션")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
}
