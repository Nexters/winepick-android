package kr.co.nexters.winepick.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.constant.Constant.REQ_CODE_GO_TO_PERSONALITY_TYPE
import kr.co.nexters.winepick.constant.Constant.STRING_EXTRA_DEEP_LINK_PERSONALITY_TYPE
import kr.co.nexters.winepick.data.repository.SurveyRepository
import kr.co.nexters.winepick.databinding.ActivitySplashBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.ui.login.LoginActivity
import kr.co.nexters.winepick.ui.type.TypeDetailActivity
import kr.co.nexters.winepick.util.startActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity(
    override val viewModel: BaseViewModel? = null
) : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    @Inject
    lateinit var surveyRepository: SurveyRepository

    private lateinit var splashView: LottieAnimationView

    private val deepLinkPersonalityType: String?
        get() = if (intent.action == Intent.ACTION_VIEW) {
            intent?.data?.getQueryParameter("personality")
        } else null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiScope.launch { surveyRepository.load() }

        splashView = findViewById(R.id.lottie_splash)
        splashView.apply {
            setAnimation("winepick.json")
            playAnimation()
            loop(true)
        }

        uiScope.launch {
            Timber.i(deepLinkPersonalityType)

            delay(DURATION)

            if (deepLinkPersonalityType != null) {
                val intent = Intent(this@SplashActivity, TypeDetailActivity::class.java)
                    .apply {
                        putExtra(
                            STRING_EXTRA_DEEP_LINK_PERSONALITY_TYPE,
                            deepLinkPersonalityType
                        )
                    }
                startActivity(intent, REQ_CODE_GO_TO_PERSONALITY_TYPE)
            }

            checkToken()
        }
    }

    private fun checkToken() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                authManager.autoLogin = false
                startActivity(LoginActivity::class, isFinish = true)
            } else if (tokenInfo != null) {
                UserApiClient.instance.me { user, error ->
                    val currentDate: Date = Date()
                    val diffDay =
                        (currentDate.time - user!!.connectedAt!!.time) / (24 * 60 * 60 * 1000)
                    if (diffDay > 14) {
                        UserApiClient.instance.unlink {
                            authManager.autoLogin = false
                            startActivity(LoginActivity::class, isFinish = true)
                        }
                    } else {
                        if (authManager.autoLogin) {
                            startActivity(HomeActivity::class, isFinish = true)
                        } else {
                            startActivity(LoginActivity::class, isFinish = true)
                        }
                    }
                }
            } else {
                authManager.autoLogin = false
                startActivity(LoginActivity::class, isFinish = true)

            }
        }
    }

    companion object {
        private const val DURATION: Long = 1500
    }
}
