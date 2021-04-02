package kr.co.nexters.winepick.ui.search

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.coroutines.launch
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.repository.SearchRepository
import kr.co.nexters.winepick.data.repository.parseKeyword
import kr.co.nexters.winepick.databinding.ActivitySearchBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.ActivityResult
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.LikeDialog
import kr.co.nexters.winepick.ui.detail.WineDetailActivity
import kr.co.nexters.winepick.util.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * 검색 화면
 *
 * @since v1.0.0 / 2021.02.06
 */
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {
    override val viewModel: SearchViewModel by viewModel()
    private val authManager: AuthManager by inject()
    private val searchRepository: SearchRepository by inject()

    private var scrollListener: EndlessScrollListener? = null

    private val searchFilters: String
        get() = intent.getStringExtra(Constant.STRING_EXTRA_SEARCH_FILTERS) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        // 입력받은 내용이 있다면 해당 내용을 필터화 하여 등록한 후 검색을 진행한다.
        if (searchFilters.isNotEmpty()) {
            val filters = searchFilters.parseKeyword()
            searchRepository.addFilterItems(filters)

            viewModel.querySearchClick(page = 0)
        }

        binding.apply {
            btnSearchBack.setOnSingleClickListener { onBackPressed() }

            rvResults.adapter = WineResultAdapter(viewModel)
            rvResults.layoutManager?.let {
                scrollListener = object : EndlessScrollListener(it, 3) {
                    override fun onLoadMore(page: Int) {
                        Timber.i("onLoadMore $page")
                        viewModel.paging()
                    }
                }
                rvResults.addOnScrollListener(scrollListener!!)
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

    fun subscribeUI() {
        viewModel.results.observe(this, {
            Timber.i("$it")
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
                            recyclerViewClear()
                            toast("검색 화면 목록 갱신 시작")
                            viewModel.querySearchClick(page = 0)
                        }
                    }
                }
                else -> Timber.i("SearchAction.NONE")
            }
        }
        viewModel.toastMessage.observe(this, Observer {
            if (it) {
                LikeDialog(
                    content = getString(R.string.like_add)
                ).show(supportFragmentManager, "LikeDialog")

                binding.apply {
                    rvResults.adapter!!.notifyDataSetChanged()
                }
            }
        })

        viewModel.cancelMessage.observe(this, Observer {
            if (it) {
                val customToast = Toast(this)
                customToast.drawCancelToast(this)
                binding.apply {
                    rvResults.adapter!!.notifyDataSetChanged()
                }
            }
        })

        viewModel.clickedWineResult.observe(this, {
            uiScope.launch {
                val intent = Intent(this@SearchActivity, WineDetailActivity::class.java)
                    .apply { putExtra("wineData", it) }

                startActivity(intent, Constant.REQ_CODE_GO_TO_DETAIL)

                viewModel.updateClickPosition(it)
            }
        })

        viewModel.initAction.subscribe {
            recyclerViewClear()
        }
    }

    private fun recyclerViewClear() {
        scrollListener?.reset()
        viewModel.prevDataClear()
    }
}
