package kr.co.nexters.winepick.ui.type

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityTypeDetailBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.type.RecentSearchAdapter
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.util.VerticalItemDecorator
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TypeDetailActivity : BaseActivity<ActivityTypeDetailBinding>(
    R.layout.activity_type_detail
) {
    override val viewModel : TypeDetailModel by viewModel<TypeDetailModel>()
    private val authManager : AuthManager by inject()
    private val searchRecycler : RecentSearchAdapter by lazy { RecentSearchAdapter() }
    private val loginViewModel : LoginViewModel by viewModel()

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Timber.e("로그인 실패 ${error}")
        }
        else if (token != null) {
            //Login Success
            Timber.d("로그인 성공")
            authManager.apply {
                this.token = token.accessToken
            }
            Timber.e("로그인성공 - 토큰 ${authManager.token}")
            loginViewModel.addUserInfo(token.accessToken)
            onResume()
        }
    }

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

        initRecycler()
    }
    private fun initRecycler() {
        binding.rvSearch.apply {
            adapter = searchRecycler
            addItemDecoration(VerticalItemDecorator(16))
        }
        if (authManager.recentSearch1 != null && !authManager.recentSearch1.isNullOrBlank() ) {
            searchRecycler.initData(listOf<String>(authManager.recentSearch1!!, authManager.recentSearch2!!))
        }
        searchRecycler.setOnItemClickListener(object : RecentSearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, data: String, pos: Int) {
              //TODO 와인 상세페이지로 넘겨야함
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

}
