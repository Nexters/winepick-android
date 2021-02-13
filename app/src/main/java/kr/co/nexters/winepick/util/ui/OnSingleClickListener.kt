package kr.co.nexters.winepick.util.ui

import android.view.View
import java.util.concurrent.atomic.AtomicBoolean

/**
 * RxJava 를 사용하지 않는 인터벌 클릭 리스너
 *
 * @since v1.0.0 / 2020.07.24
 */
class OnSingleClickListener(
    private val clickListener: () -> Unit,
    private val intervalMs: Long = 1000
) : View.OnClickListener {
    private var canClick = AtomicBoolean(true)

    override fun onClick(v: View?) {
        if (canClick.getAndSet(false)) {
            v?.run {
                postDelayed({ canClick.set(true) }, intervalMs)
                clickListener()
            }
        }
    }
}
