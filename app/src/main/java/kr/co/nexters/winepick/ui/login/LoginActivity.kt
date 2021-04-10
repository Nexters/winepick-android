package kr.co.nexters.winepick.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.library.baseAdapters.BR
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityLoginBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.util.startActivity
import kr.co.nexters.winepick.util.toast


import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(
    R.layout.activity_login
) {
    override val viewModel: LoginViewModel by viewModels<LoginViewModel>()

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
            }
            tvGuest.setOnClickListener {
                authManager.token = "guest"
                startActivity(HomeActivity::class, true)
            }
        }

        viewModel.toastMessageText.observe(this, { if (it.isNotEmpty()) toast(it) })
    }
}
