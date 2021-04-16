package kr.co.nexters.winepick.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kr.co.nexters.winepick.ui.base.BaseActivity
import kotlin.reflect.KClass

inline fun <reified T> BaseActivity<*>.startActivity(clazz: KClass<out T>, isFinish: Boolean = false, flags: Int? = null) where T : AppCompatActivity {
    Intent(this, clazz.java).apply {
        flags?.let { this.flags = it }
    }.run { startActivity(this) }
    if (isFinish) finish()
}

inline fun <reified T> Fragment.startActivity(clazz: KClass<out T>, isFinish: Boolean = false, flags: Int? = null) where T : AppCompatActivity {
    Intent(requireActivity(), clazz.java).apply {
        flags?.let { this.flags = it }
    }.run { startActivity(this) }
    if (isFinish) requireActivity().finish()
}
