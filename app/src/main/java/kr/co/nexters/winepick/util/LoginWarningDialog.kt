package kr.co.nexters.winepick.util

import android.content.Context
import android.view.LayoutInflater
import com.kakao.sdk.template.model.Content
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication

fun initLoginWarningDialog(context : Context) {
    val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_login_warning, null)
    


    dialog.apply {
        setView(view)
        setCancelable(false)
        show()
    }
}