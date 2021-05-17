package kr.co.nexters.winepick.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.library.baseAdapters.BR
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityLoginBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.util.startActivity
import kr.co.nexters.winepick.util.toast

/**
 * 로그인 화면
 *
 * @since v1.0.0 / 2021.02.08
 */
@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override val viewModel: LoginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.apply {
            btnLogin.setOnClickListener {
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
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
