package kr.co.nexters.winepick.util

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.nexters.winepick.R

fun Toast.drawCustomToast(activity: Activity){
    val inflater : LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val layout = inflater.inflate(R.layout.dialog_like_cancel,activity.findViewById(R.id.cl_like_cancel))

    setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL,0,0)
    duration = Toast.LENGTH_SHORT
    view = layout
    show()
}
