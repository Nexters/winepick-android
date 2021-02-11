package kr.co.nexters.winepick.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.databinding.ActivitySearchFilterBinding
import kr.co.nexters.winepick.ui.base.BaseActivity

/**
 * 검색 필터 변경 화면
 *
 * @since v1.0.0 / 2021.02.11
 */
class SearchFilterActivity : BaseActivity<ActivitySearchFilterBinding>(
    R.layout.activity_search_filter
) {
    override val viewModel: SearchFilterViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchFilterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        binding.apply {

        }
    }

    override fun onBackPressed() {
        setResult(
            Constant.REQ_CODE_GO_TO_FILTER,
            Intent().apply { putExtra(Constant.INT_EXTRA_FILTER_NUM, 3) })
        super.onBackPressed()
    }
}
