package kr.co.nexters.winepick.util

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(msg: String) {
    Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
}

fun Application.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}