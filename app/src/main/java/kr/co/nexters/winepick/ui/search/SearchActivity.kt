package kr.co.nexters.winepick.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.databinding.ActivitySearchBinding
import kr.co.nexters.winepick.ui.base.ActivityResult
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.util.EndlessScrollListener
import kr.co.nexters.winepick.util.hideKeyboard
import kr.co.nexters.winepick.util.setOnSingleClickListener
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
            btnSearchBack.setOnSingleClickListener { onBackPressed() }

            rvResults.adapter = SearchResultAdapter(viewModel)
            rvResults.layoutManager?.let {
                rvResults.addOnScrollListener(object : EndlessScrollListener(it, 3) {
                    override fun onLoadMore(page: Int) {
                        viewModel.paging()
                    }
                })
            }
            rvCurrents.adapter = SearchRelativeAdapter(viewModel)
            rvRecommends.adapter = SearchRecommendAdapter(
                viewModel,
                resources.getStringArray(R.array.search_recommends)
            )
        }

        subscribeUI()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.REQ_CODE_GO_TO_FILTER) {
            deferred.complete(ActivityResult(resultCode, data))
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun subscribeUI() {
        viewModel.results.observe(this, {

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
                    toast("필터 변경 화면으로 이동")
                    binding.etQuery.clearFocus()

                    uiScope.launch {
                        val intent = Intent(this@SearchActivity, SearchFilterActivity::class.java)
                        val result = startActivity(intent, Constant.REQ_CODE_GO_TO_FILTER)

                        val needToUpdate = result.data?.getBooleanExtra(
                            Constant.BOOL_EXTRA_SEARCH_NEED_UPDATE,
                            false
                        ) ?: false

                        Timber.i("$needToUpdate")

                        if (needToUpdate) {
                            toast("검색 화면 목록 갱신 시작")
                            viewModel.querySearchClick(pageNumber = 0)
                        }
                    }
                }
                else -> Timber.i("SearchAction.NONE")
            }
        }
    }
}
