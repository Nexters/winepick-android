package kr.co.nexters.winepick.example.kotlin.normal

import android.os.Bundle
import android.widget.Toast
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.base.BaseActivity
import kr.co.nexters.winepick.base.BaseViewModel
import kr.co.nexters.winepick.base.navigate
import kr.co.nexters.winepick.databinding.ActivityKotlinDefaultBinding

/**
 * Kotlin 에서 사용하는 일반 액티비티 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class KotlinDefaultActivity : BaseActivity<ActivityKotlinDefaultBinding>(
    R.layout.activity_kotlin_default
) {
    override val viewModel: BaseViewModel?
        get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            // 텍스트 값 세팅
            tvTitle.text = "코틀린 액티비티 테스트"

            // 클릭 리스너 사용 예 (with Lambda)
            tvTitle.setOnClickListener {
                Toast.makeText(this@KotlinDefaultActivity, binding.tvTitle.text, Toast.LENGTH_SHORT)
                    .show()
            }

            // 특정 프레임 레이아웃에 Fragment 을 로딩하기 위해 사용하는 로직 (Fragment 사용안할 시 필요 없는 코드)
            KotlinDefaultFragment().navigate(supportFragmentManager, flFragment.id)
        }
    }
}
