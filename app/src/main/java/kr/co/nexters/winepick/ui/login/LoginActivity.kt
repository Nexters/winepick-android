package kr.co.nexters.winepick.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
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


    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e("로그인 실패 ${error}")
            //Login Fail
        }
        else if (token != null) {
            //Login Success
            Timber.d("로그인 성공")
            authManager.apply {
                this.token = token.accessToken
            }
            Timber.e("로그인성공 - 토큰 ${authManager.token}")
            Intent(applicationContext,HomeActivity::class.java).apply {
                putExtra("mode","user")
            }.run { startActivity(this) }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.apply {
            btnLogin.setOnClickListener {
                LoginClient.instance.run {
                    if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                        loginWithKakaoTalk(this@LoginActivity, callback = callback)
                    } else {
                        loginWithKakaoAccount(this@LoginActivity, callback = callback)
                    }
                }
                tvGuest.setOnClickListener {
                    Intent(applicationContext, HomeActivity::class.java).apply {
                        putExtra("mode", "guest")
                    }.run { startActivity(this) }

                }
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
}
