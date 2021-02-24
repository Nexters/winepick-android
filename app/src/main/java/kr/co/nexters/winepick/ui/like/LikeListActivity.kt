package kr.co.nexters.winepick.ui.like

import android.os.Bundle
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityLikeListBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.search.WineResultAdapter
import kr.co.nexters.winepick.util.VerticalItemDecorator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikeListActivity : BaseActivity<ActivityLikeListBinding>(
    R.layout.activity_like_list
) {
    private val likeViewModel: LikeViewModel by viewModel<LikeViewModel>()
    private val wineLikeAdpater: WineResultAdapter by lazy { WineResultAdapter(likeViewModel) }
    override val viewModel: LikeViewModel by viewModel<LikeViewModel>()
    private val authManager: AuthManager by inject()
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
        viewModel.results.observe(this, { data ->
            data?.let {

            }
        })
    }
}
