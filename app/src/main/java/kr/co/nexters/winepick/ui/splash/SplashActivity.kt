package kr.co.nexters.winepick.ui.splash

import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.repository.SurveyRepository
import kr.co.nexters.winepick.databinding.ActivitySplashBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.ui.login.LoginActivity
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.inject
import java.util.*

class SplashActivity(
    override val viewModel: BaseViewModel? = null
) : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val authManager : AuthManager by inject()
    private val surveyRepository : SurveyRepository by inject()

    private lateinit var splashView : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiScope.launch {
            surveyRepository.load()
        }

        splashView = findViewById(R.id.lottie_splash)
        splashView.apply {
            setAnimation("winepick.json")
            playAnimation()
            loop(true)
        }

        Handler(mainLooper).postDelayed({
            checkToken()
        },DURATION)

    }
    private fun checkToken() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                authManager.autoLogin = false
                startActivity(LoginActivity::class,isFinish = true)
            }
            else if (tokenInfo!=null) {
                UserApiClient.instance.me { user, error ->
                    val currentDate : Date = Date()
                    val diffDay = (currentDate.time - user!!.connectedAt!!.time) / (24*60*60*1000)
                    if(diffDay>14) {
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
                startActivity(LoginActivity::class,isFinish = true)

            }
        }
    }

    companion object {
        private const val DURATION : Long = 1500
    }
}
