package kr.co.nexters.winepick.ui.like

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityLikeListBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.search.WineResultAdapter
import kr.co.nexters.winepick.util.drawCancelToast

@AndroidEntryPoint
class LikeListActivity : BaseActivity<ActivityLikeListBinding>(
    R.layout.activity_like_list
) {
    private val wineLikeAdpater: WineResultAdapter by lazy { WineResultAdapter(viewModel,authManager) }
    override val viewModel: LikeViewModel by viewModels<LikeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        viewModel.backButton.observe(this, Observer {
            if (it) finish()
        })
        initLikeRecycer()

        viewModel.cancelMessage.observe(this, Observer {
            if(it) {
                val customToast = Toast(this)
                customToast.drawCancelToast(this)
                binding.apply {
                    rvLikeList.adapter!!.notifyDataSetChanged()
                }
            }
        })

    }

    fun initLikeRecycer() {
        binding.rvLikeList.apply {
            adapter = wineLikeAdpater
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
