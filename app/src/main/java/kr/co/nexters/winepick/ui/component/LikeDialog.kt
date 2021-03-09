package kr.co.nexters.winepick.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.DialogConfirmDialogBinding
import kr.co.nexters.winepick.databinding.DialogLikeBinding
import kr.co.nexters.winepick.ui.splash.SplashActivity
import kr.co.nexters.winepick.util.dpToPx
import kr.co.nexters.winepick.util.setGone


/**
 * 두개의 버튼을 가지고 있는 DialogFragment
 *
 * @see [Android, DialogFragment 커스텀 다이얼로그 프래그먼트 만들기](https://black-jin0427.tistory.com/283)
 * @param width Dialog 너비
 * @param height Dialog 높이
 * @param title Dialog 제목
 * @param content Dialog 컨텐츠
 *
 * @since v1.0.0 / 2021.02.22
 */
class LikeDialog(
    private val width: Int = 196.dpToPx(),
    private val height: Int = 254.dpToPx(),
    private val title: String = "",
    private val content: String = ""
) : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: DialogLikeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogLikeBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (dialog == null) {
            return
        }

        dialog?.window?.setWindowAnimations(R.style.DialogFadeAnimation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content = content

        Handler().postDelayed({
            dialog?.dismiss()
        }, LikeDialog.DURATION)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(width, height)
    }

    companion object {
        private const val DURATION : Long = 1000
    }
}
