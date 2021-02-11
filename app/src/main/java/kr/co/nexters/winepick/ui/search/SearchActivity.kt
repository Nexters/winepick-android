package kr.co.nexters.winepick.ui.search

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivitySearchBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.util.hideKeyboard
import kr.co.nexters.winepick.util.toast
import timber.log.Timber

/**
 * 검색 화면
 *
 * @since v1.0.0 / 2021.02.06
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {
    override val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        binding.apply {
            rvResults.adapter = SearchResultAdapter(viewModel)
            rvCurrents.adapter = SearchRelativeAdapter(viewModel)
            rvRecommends.adapter = SearchRecommendAdapter(viewModel)
        }

        subscribeUI()
    }

    fun subscribeUI() {
        viewModel.results.observe(this, {
            binding.tvResultTitle.text =
                String.format(getString(R.string.search_result_title), it.size)
        })

        viewModel.searchFrontPage.observe(this, {
            if (it == SearchFront.DEFAULT) {
                binding.etQuery.hideKeyboard()
            }
        })

        viewModel.searchAction.subscribe {
            when (it) {
                SearchAction.QUERY_RESET -> {
                    binding.etQuery.text?.clear()
                    binding.etQuery.clearFocus()
                }
                SearchAction.QUERY_SEARCH -> binding.etQuery.clearFocus()
                SearchAction.EDIT_FILTER -> {
                    // TODO 필터 변경 화면으로 이동하는 로직 구현하기
                    toast("필터 변경 화면으로 이동")
                    binding.etQuery.clearFocus()
                }
                else -> Timber.i("SearchAction.NONE")
            }
        }
    }
}
