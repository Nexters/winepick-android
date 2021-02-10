package kr.co.nexters.winepick.example.kotlin.viewmodel

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.base.navigate
import kr.co.nexters.winepick.databinding.ActivityKotlinViewmodelBinding

/**
 * Kotlin 에서 사용하는 일반 액티비티 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class KotlinViewModelActivity : BaseActivity<ActivityKotlinViewmodelBinding>(
    R.layout.activity_kotlin_viewmodel
) {
    override val viewModel: KotlinViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(KotlinViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        binding.apply {
            // 텍스트 값 세팅 (ViewModel 에서 진행하므로 해당 코드 제거)
            // tvTitle.text = "코틀린 액티비티 테스트"

            // 클릭 리스너 사용 예 (with Lambda)
            tvTitle.setOnClickListener {
                Toast.makeText(
                    this@KotlinViewModelActivity,
                    binding.tvTitle.text,
                    Toast.LENGTH_SHORT
                ).show()
            }

            // 특정 프레임 레이아웃에 Fragment 을 로딩하기 위해 사용하는 로직 (Fragment 사용안할 시 필요 없는 코드)
            KotlinViewModelFragment().navigate(supportFragmentManager, flFragment.id)
        }

        // 아래와 같은 방식으로 viewModel 내 LiveData 값이 바뀔때마다 observing 가능
        viewModel.title.observe(this, { title ->
            Toast.makeText(
                this@KotlinViewModelActivity,
                "$title 변경 완료",
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}
