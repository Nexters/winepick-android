package kr.co.nexters.winepick.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.ui.survey.SurveyActivity
import kr.co.nexters.winepick.util.dpToPx
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    override val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private val authManager: AuthManager by inject()
    private val loginViewModel: LoginViewModel by viewModel()

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e("로그인 실패 ${error}")
        } else if (token != null) {
            //Login Success
            Timber.d("로그인 성공")
            authManager.apply {
                this.token = token.accessToken
            }
            UserApiClient.instance.me { user, error ->
                val kakaoId = user!!.id
                loginViewModel.addUserInfo(token.accessToken, authManager.testType, kakaoId)
            }
            Timber.d("로그인성공 - 토큰 ${authManager.token}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        viewModel.loginWarningDlg.observe(this, Observer {
            if (it) {
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

        viewModel.currentSurvey.observe(this, {
            if (it == null) {
                WinePickApplication.getGlobalApplicationContext().startActivity(
                    Intent(WinePickApplication.appContext, SurveyActivity::class.java)
                        .apply { putExtra(Constant.BOOL_EXTRA_SURVEY_RESET, true) }
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } else if (it.number == -1) {
                ConfirmDialog(
                    241.dpToPx(),
                    202.dpToPx(),
                    "이전에 검사를 완료했습니다.",
                    "다시 나와 어울리는 와인을\n찾아보려면 확인을 눌러주세요",
                    "취소",
                    { dialogFragment: DialogFragment? ->
                        dialogFragment?.dismiss()
                    },
                    "확인",
                    { dialogFragment: DialogFragment? ->
                        WinePickApplication.getGlobalApplicationContext().startActivity(
                            Intent(WinePickApplication.appContext, SurveyActivity::class.java)
                                .apply { putExtra(Constant.BOOL_EXTRA_SURVEY_RESET, true) }
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        dialogFragment?.dismiss()
                    },
                    false
                ).show(supportFragmentManager, "SurveyReset")
            } else {
                ConfirmDialog(
                    241.dpToPx(),
                    202.dpToPx(),
                    "이전 테스트 내역이 있습니다.",
                    "이전에 테스트 했던 내용부터\n다시 시작하려면 불러옴을 눌러주세요",
                    "불러옴",
                    { dialogFragment: DialogFragment? ->
                        WinePickApplication.getGlobalApplicationContext().startActivity(
                            Intent(WinePickApplication.appContext, SurveyActivity::class.java)
                                .apply { putExtra(Constant.BOOL_EXTRA_SURVEY_RESET, false) }
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        dialogFragment?.dismiss()
                    },
                    "새로 시작",
                    { dialogFragment: DialogFragment? ->
                        WinePickApplication.getGlobalApplicationContext().startActivity(
                            Intent(WinePickApplication.appContext, SurveyActivity::class.java)
                                .apply { putExtra(Constant.BOOL_EXTRA_SURVEY_RESET, true) }
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        dialogFragment?.dismiss()
                    },
                    false
                ).show(supportFragmentManager, "SurveyTestExisted")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
