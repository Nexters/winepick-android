package kr.co.nexters.winepick.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.template.model.Content
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.di.authModule
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.ui.login.LoginActivity
import timber.log.Timber

fun initLoginWarningDialog(context : Context, authManager: AuthManager) {
    val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_login_warning, null)
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
            Intent(context,HomeActivity::class.java).apply {
                putExtra("mode","user")
            }.run { startActivity(context,this,null) }
        }
    }
    view.findViewById<TextView>(R.id.tv_login_warning_no).setOnClickListener {
        dialog.cancel()
    }
    view.findViewById<TextView>(R.id.tv_login_warning_yes).setOnClickListener {
        LoginClient.instance.run {
            if (isKakaoTalkLoginAvailable(context)) {
                loginWithKakaoTalk(context, callback = callback )
            } else {
                loginWithKakaoAccount(context, callback = callback)
            }
        }
    }
    dialog.apply {
        setView(view)
        setCancelable(false)
        show()
    }
}