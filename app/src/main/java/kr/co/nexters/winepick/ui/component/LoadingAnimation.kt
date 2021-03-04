package kr.co.nexters.winepick.ui.component

import android.app.Dialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.FrameLayout
import android.widget.ProgressBar
import kotlinx.coroutines.*
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.util.dpToPx
import kr.co.nexters.winepick.util.ext.getColor
import timber.log.Timber

/**
 * 로딩 중에 보여줄 이미지
 *
 * @since v1.0.0 / 2021.02.23
 */
object LoadingAnimation {
    var activity: BaseActivity<*>? = null
    var dialog: Dialog? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    val loadingScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + this.exceptionHandler)

    /** 로딩 애니메이션 켜기 Runnable */
    private fun showDialogJob() = loadingScope.launch(exceptionHandler) {
        Timber.i("show DialogJob")

        // activity 가 정상이 아니거나, activity 가 이미 소멸된 상태라면 더 이상 진행하지 않음
        if (activity!!.isFinishing || activity!!.isDestroyed) {
            this.cancel()
            return@launch
        }

        // dialog 가 null 이거나 보이고 있지 않을 경우에는 dialog 인스턴스를 새로 만들어서 보여준다.
        if (dialog == null || !dialog!!.isShowing) {
            // dialog 초기화
            dialog = Dialog(activity!!)
                .apply { window?.setBackgroundDrawableResource(R.color.transparent) }

            // progressbar 색상 설정
            val progressBar = ProgressBar(activity).apply {
                isIndeterminate = true
                PorterDuffColorFilter(
                    R.color.loading_animation_progressbar_solid.getColor(),
                    PorterDuff.Mode.MULTIPLY
                ).let { indeterminateDrawable.colorFilter = it }

                setBackgroundResource(R.color.transparent)
            }

            // progressbar 크기 설정
            val params = FrameLayout.LayoutParams(56.dpToPx(), 56.dpToPx())

            // progressbar dialog 에 넣고 보여주기
            dialog?.addContentView(progressBar, params)
            dialog?.show()
            Timber.i("show Loading Dialog")
        }

        delay(3000)

        dismiss()
    }

    /** 로딩 애니메이션 켜기 Runnable */
    private fun dismissDialogJob() = loadingScope.launch(exceptionHandler) {
        Timber.i("dissmiss DialogJob")

        if (dialog?.isShowing == true) {
            if (activity?.isFinishing != true && activity?.isDestroyed != true)
                dialog?.dismiss()
        }
        dialog = null
    }

    /**
     * 로딩 애니메이션 보이기
     *
     * viewModel 을 사용하는 화면에서 이 함수를 호출해야 할 시
     * 이 함수를 직접 호출하지 않고 각 화면과 ViewModel 에 있는 [showLoading()] 메서드를 활용한다.
     */
    fun show(activity: BaseActivity<*>): Job = loadingScope.launch {
        Timber.i("LoadingAnimation show")
        this@LoadingAnimation.activity = activity

        showDialogJob()
    }

    /**
     * 로딩 애니메이션 끄기
     *
     * viewModel 을 사용하는 화면에서 이 함수를 호출해야 할 시
     * 이 함수를 직접 호출하지 않고 각 화면과 ViewModel 에 있는 [hideLoading()] 메서드를 활용한다.
     */
    fun dismiss(): Job = loadingScope.launch {
        Timber.i("LoadingAnimation dismiss")
        loadingScope.launch {
            this.coroutineContext.cancelChildren()
            dismissDialogJob()
            this@LoadingAnimation.activity = null
        }
    }
}
