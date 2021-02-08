package kr.co.nexters.winepick.example.kotlin.viewmodel

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.base.BaseFragment
import kr.co.nexters.winepick.databinding.FragmentKotlinViewmodelBinding

/**
 * Kotlin 에서 사용하는 일반 프레그먼트 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class KotlinViewModelFragment : BaseFragment<FragmentKotlinViewmodelBinding>(
    R.layout.fragment_kotlin_viewmodel
) {
    override val viewModel: KotlinViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(KotlinViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        binding.apply {
            // 텍스트 값 세팅 (ViewModel 에 요청하여 바꾸는 것도 가능하긴 함)
            viewModel.editTitle("코틀린 프레그먼트 테스트")

            // 클릭 리스너 사용 예 (with Lambda)
            tvTitle.setOnClickListener {
                Toast.makeText(activity, binding.tvTitle.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
