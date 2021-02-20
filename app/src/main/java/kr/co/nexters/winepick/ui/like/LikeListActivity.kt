package kr.co.nexters.winepick.ui.like

import android.os.Bundle
import android.view.View
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityLikeListBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.type.RecentSearchAdapter
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.ui.search.SearchResultAdapter
import kr.co.nexters.winepick.ui.search.SearchViewModel
import kr.co.nexters.winepick.util.VerticalItemDecorator
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikeListActivity : BaseActivity<ActivityLikeListBinding>(
    R.layout.activity_like_list
) {
    private val searchViewModel : SearchViewModel by viewModel<SearchViewModel>()
    private val wineLikeAdpater : SearchResultAdapter by lazy { SearchResultAdapter(searchViewModel) }
    override val viewModel : LikeViewModel by viewModel<LikeViewModel>()
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        initLikeRecycer()

    }
    fun initLikeRecycer() {
        binding.rvLikeList.apply {
            adapter = wineLikeAdpater
            addItemDecoration(VerticalItemDecorator(16))
        }
        //TODO 좋아요 리사이클러뷰 data
        viewModel.wineList.observe(this,{ data ->
            data?.let{
               searchViewModel.likesWineResult(it)
            }
        })
    }
}
