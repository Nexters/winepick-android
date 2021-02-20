package kr.co.nexters.winepick.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.home.HomeActivity
import timber.log.Timber

class KakaoLoginHelper(private val context: Context, private val authManager: AuthManager) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e("로그인 실패 ${error}")
            //Login Fail
        } else if (token != null) {
            //Login Success
            Timber.d("로그인 성공")
            authManager.apply {
                this.token = token.accessToken
            }
            Timber.e("로그인성공 - 토큰 ${authManager.token}")
            Intent(context, HomeActivity::class.java).apply {
                putExtra("mode", "user")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }.run { ContextCompat.startActivity(context, this, null) }
        }
    }

    fun goLogin() {
        LoginClient.instance.run {
            if (isKakaoTalkLoginAvailable(context)) {
                loginWithKakaoTalk(context, callback = callback)
            } else {
                loginWithKakaoAccount(context, callback = callback)
            }
        }
    }


}