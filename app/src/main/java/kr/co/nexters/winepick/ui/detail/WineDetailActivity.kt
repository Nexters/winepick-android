package kr.co.nexters.winepick.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.kakao.sdk.auth.LoginClient
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.constant.Constant
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.databinding.ActivityWineDetailBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.component.LikeDialog
import kr.co.nexters.winepick.ui.webview.WebViewActivity
import kr.co.nexters.winepick.util.HorizontalItemDecorator
import kr.co.nexters.winepick.util.VerticalItemDecorator
import kr.co.nexters.winepick.util.drawCancelToast
import kr.co.nexters.winepick.util.setOnSingleClickListener

/**
 * 와인 상세 화면
 *
 * @since v1.0.0 / 2021.03.15
 */
@AndroidEntryPoint
class WineDetailActivity : BaseActivity<ActivityWineDetailBinding>(R.layout.activity_wine_detail) {
    override val viewModel: WineDetailViewModel by viewModels<WineDetailViewModel>()
    private val wineFoodAdapter: WineFoodAdapter by lazy {
        WineFoodAdapter(WineListType.FOOD, viewModel)
    }
    private val wineValueAdapter: WineValueAdapter by lazy { WineValueAdapter(viewModel) }
    private val wineFeatureAdapter: WineFoodAdapter by lazy {
        WineFoodAdapter(WineListType.FEATURE, viewModel)
    }

    /** 이전 화면에서부터 받은 와인 아이템[WineResult] */
    private val wineResult: WineResult?
        get() = intent.getSerializableExtra("wineData") as? WineResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wineResult?.let { viewModel.getWineResult(it) } ?: finish()

        binding.setVariable(BR.vm, viewModel)
        initFoodRecycler()
        viewModel.toastMessage.observe(this, Observer {
            if (it) {
                LikeDialog(
                    content = getString(R.string.like_add)
                ).show(supportFragmentManager, "LikeDialog")

            }
        })

        viewModel.cancelMessage.observe(this, Observer {
            if (it) {
                val customToast = Toast(this)
                customToast.drawCancelToast(this)
            }
        })

        viewModel.backButton.observe(this, Observer {
            if (it) {
                finish()
            }
        })

        viewModel.loginWarningDlg.observe(this, Observer {
            if (it) {
                ConfirmDialog(
                    title = getString(R.string.login_warning_title),
                    content = getString(R.string.login_warning_like),
                    leftText = getString(R.string.login_warning_btn_left_text),
                    leftClickListener = {
                        it.dismiss()
                    },
                    rightText = getString(R.string.login_warning_btn_right_text),
                    rightClickListener = {
                        LoginClient.instance.run {
                            if (isKakaoTalkLoginAvailable(this@WineDetailActivity)) {
                                loginWithKakaoTalk(this@WineDetailActivity, callback = callback)
                            } else {
                                loginWithKakaoAccount(this@WineDetailActivity, callback = callback)
                            }
                        }
                        it.dismiss()

                    },
                    cancelable = false
                ).show(supportFragmentManager, "LoginWarningDialog")
            }
        })

        viewModel.wineFeature.observe(this, {
            (binding.rvWineFeature.layoutManager as GridLayoutManager).spanCount = when (it.size) {
                1 -> 1
                2 -> 2
                else -> 3
            }
        })

    }

    fun initFoodRecycler() {
        binding.rvWineFood.apply {
            adapter = wineFoodAdapter
            addItemDecoration(HorizontalItemDecorator(8))
            addItemDecoration(VerticalItemDecorator(20))

        }

        binding.rvWineFeature.apply {
            adapter = wineFeatureAdapter
            addItemDecoration(HorizontalItemDecorator(8))
            addItemDecoration(VerticalItemDecorator(20))
        }

        binding.rvWineValue.apply {
            adapter = wineValueAdapter
            addItemDecoration(VerticalItemDecorator(10))
        }

        binding.clWineImg.setOnSingleClickListener {
            val intent = Intent(this@WineDetailActivity, WebViewActivity::class.java)
                .putExtra(
                    Constant.STRING_EXTRA_WEB_URL_PARAMS,
                    wineResult?.nmKor ?: wineResult?.nmEng
                )

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
