package kr.co.nexters.winepick.example.kotlin.normal

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.ui.base.BaseFragment
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.databinding.FragmentKotlinDefaultBinding

/**
 * Kotlin 에서 사용하는 일반 프레그먼트 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class KotlinDefaultFragment : BaseFragment<FragmentKotlinDefaultBinding>(
    R.layout.fragment_kotlin_default
) {
    override val viewModel: BaseViewModel?
        get() = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // 텍스트 값 세팅
            tvTitle.text = "코틀린 프레그먼트 테스트"

            // 클릭 리스너 사용 예 (with Lambda)
            tvTitle.setOnClickListener {
                Toast.makeText(activity, binding.tvTitle.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
