package kr.co.nexters.winepick.util

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.ui.survey.SurveyActivity

fun initReSurveyNotiDialog(context: Context) {
    val dialog = androidx.appcompat.app.AlertDialog.Builder(context).create()
    val view = LayoutInflater.from(context).inflate(R.layout.dialog_resurvey_noti, null)

    view.findViewById<TextView>(R.id.tv_login_warning_no).setOnClickListener {
        dialog.cancel()
    }
    view.findViewById<TextView>(R.id.tv_login_warning_yes).setOnClickListener {
        val intent = Intent(context, SurveyActivity::class.java)
        context.startActivity(intent)
        dialog.cancel()
    }
    dialog.apply {
        setView(view)
        setCancelable(false)
        show()
    }
}