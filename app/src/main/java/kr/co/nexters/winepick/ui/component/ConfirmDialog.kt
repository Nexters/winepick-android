package kr.co.nexters.winepick.ui.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import kr.co.nexters.winepick.databinding.DialogConfirmDialogBinding
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
 * @param leftText Dialog 왼쪽 버튼 텍스트
 * @param leftClickListener Dialog 왼쪽 버튼 클릭 리스너
 * @param rightText Dialog 오른쪽 버튼 텍스트
 * @param rightClickListener Dialog 오른쪽 버튼 클릭 리스너
 * @param cancelable Dim 영역을 클릭했을 때 dialog 를 꺼지게 할 것인지 여부
 *                   false 일 시, 화면 밖을 눌러도 dismiss 되지 않는다.
 *
 * @since v1.0.0 / 2021.02.22
 */
class ConfirmDialog(
    private val width: Int = 241.dpToPx(),
    private val height: Int = 202.dpToPx(),
    private val title: String = "",
    private val content: String = "",
    private val leftText: String,
    private val leftClickListener: ((DialogFragment) -> Unit)? = null,
    private val rightText: String,
    private val rightClickListener: ((DialogFragment) -> Unit)? = null,
    private val cancelable: Boolean = false
) : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = cancelable
    }

    private lateinit var binding: DialogConfirmDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title = title
        binding.content = content
        binding.btnLeftText = leftText
        binding.btnRightText = rightText

        if (title.isEmpty()) binding.tvConfirmDialogTitle.setGone()
        if (content.isEmpty()) binding.tvConfirmDialogContent.setGone()

        binding.btnConfirmDialogLeft.setOnClickListener {
            leftClickListener?.let {
                it(this)
            } ?: kotlin.run {
                dismiss()
            }
        }

        binding.btnConfirmDialogRight.setOnClickListener {
            rightClickListener?.let {
                it(this)
            } ?: kotlin.run {
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(width, height)
    }
}
