package kr.co.nexters.winepick.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.detail.DetailActivity
import kr.co.nexters.winepick.ui.login.LoginActivity
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.ui.survey.SurveyActivity
import kr.co.nexters.winepick.util.drawLikeToast
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    override val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private val authManager: AuthManager by inject()
    val loginViewModel : LoginViewModel by viewModel()

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e("로그인 실패 ${error}")
        }
        else if (token != null) {
            //Login Success
            Timber.d("로그인 성공")
            authManager.apply {
                this.token = token.accessToken
            }
            UserApiClient.instance.me { user, error ->
                val kakaoId = user!!.id
                loginViewModel.addUserInfo(token.accessToken,authManager.testType, kakaoId)
            }
            Timber.d("로그인성공 - 토큰 ${authManager.token}")

            onResume()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        viewModel.loginWarningDlg.observe(this, Observer {
            if(it) {
                ConfirmDialog(
                        title = getString(R.string.login_warning_title),
                        content = getString(R.string.login_warning_like),
                        leftText = getString(R.string.login_warning_btn_left_text),
                        leftClickListener = {
                            it.dismiss()
                        },
                        rightText = getString(R.string.login_warning_btn_right_text),
                        rightClickListener = {
                            LoginClient.instance.run {
                                if (isKakaoTalkLoginAvailable(this@HomeActivity)) {
                                    loginWithKakaoTalk(this@HomeActivity, callback = callback)
                                } else {
                                    loginWithKakaoAccount(this@HomeActivity, callback = callback)
                                }
                            }
                            it.dismiss()

                        },
                        cancelable = false
                ).show(supportFragmentManager, "LoginWarningDialog")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()

        startActivity(Intent(this, DetailActivity::class.java))
    }
}
