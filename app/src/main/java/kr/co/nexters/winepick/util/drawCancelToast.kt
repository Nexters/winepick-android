package kr.co.nexters.winepick.util

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.ui.search.SearchViewModel

fun Toast.drawCancelToast(activity: Activity){
    val inflater : LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.dialog_like_cancel,activity.findViewById(R.id.cl_like_cancel))

    setGravity(Gravity.BOTTOM and Gravity.CENTER,0,0)
    duration = Toast.LENGTH_SHORT
    view = layout
    show()
}
