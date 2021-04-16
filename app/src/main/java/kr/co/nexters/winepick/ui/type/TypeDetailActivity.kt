package kr.co.nexters.winepick.ui.type

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.kakao.sdk.auth.LoginClient
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.constant.Constant
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.repository.WineRepository
import kr.co.nexters.winepick.databinding.ActivityTypeDetailBinding
import kr.co.nexters.winepick.type.RecentSearchAdapter
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.detail.WineDetailActivity
import kr.co.nexters.winepick.ui.survey.SurveyActivity
import kr.co.nexters.winepick.util.VerticalItemDecorator
import javax.inject.Inject

@AndroidEntryPoint
class TypeDetailActivity : BaseActivity<ActivityTypeDetailBinding>(
    R.layout.activity_type_detail
) {
    override val viewModel : TypeDetailModel by viewModels<TypeDetailModel>()
    private val searchRecycler : RecentSearchAdapter by lazy { RecentSearchAdapter() }
    @Inject lateinit var wineRepository : WineRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.vm!!.setUserPersonalType()
        viewModel.backButton.observe(this, {
            if (it) finish()
        })

        viewModel.loginWarningDlg.observe(this, Observer {
            if(it) {
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
                                if (isKakaoTalkLoginAvailable(this@TypeDetailActivity)) {
                                    loginWithKakaoTalk(this@TypeDetailActivity, callback = callback)
                                } else {
                                    loginWithKakaoAccount(this@TypeDetailActivity, callback = callback)
                                }
                            }
                            it.dismiss()

                        },
                        cancelable = false
                ).show(supportFragmentManager, "LoginWarningDialog")
            }
        })

        viewModel.testWarningDlg.observe(this, Observer {
            if(it) {
                ConfirmDialog(
                    title = getString(R.string.test_warning_title),
                    content = getString(R.string.test_warning_content),
                    leftText = getString(R.string.test_warning_btn_left_text),
                    leftClickListener = {
                        it.dismiss()
                    },
                    rightText = getString(R.string.test_warning_btn_right_text),
                    rightClickListener = {
                        authManager.testType = "N"
                        Intent(WinePickApplication.appContext, SurveyActivity::class.java).apply {
                            putExtra(Constant.BOOL_EXTRA_SURVEY_RESET, true)
                        }.run {
                            WinePickApplication.getGlobalApplicationContext().startActivity(this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                            finish()
                        }
                        it.dismiss()

                    },
                    cancelable = false
                ).show(supportFragmentManager, "LoginWarningDialog")
            }
        })

        initRecycler()
    }
    private fun initRecycler() {
        binding.rvSearch.apply {
            adapter = searchRecycler
            addItemDecoration(VerticalItemDecorator(16))
        }
        val userViewWines: List<WineResult> = wineRepository.userViewWines
        searchRecycler.initData(userViewWines)
        searchRecycler.setOnItemClickListener(object : RecentSearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: WineResult, pos: Int) {
                val intent = Intent(this@TypeDetailActivity, WineDetailActivity::class.java)
                intent.putExtra("wineData", data)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

}
