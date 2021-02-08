package kr.co.nexters.winepick.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Int.pxToDp() : Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx() : Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.pxToSp() : Int = (this / Resources.getSystem().displayMetrics.scaledDensity).toInt()

fun Int.spToPx() : Int = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()


fun Fragment.hideKeyboard() {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)

    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun AppCompatActivity.hideKeyboard() {
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}