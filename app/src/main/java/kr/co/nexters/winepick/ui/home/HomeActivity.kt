package kr.co.nexters.winepick.ui.home

import android.content.Intent
import android.os.Bundle
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {
    override val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.vm!!.getUserLikes()

        //TODO test를 위해 임의로 만들어놈
        if (authManager.testType != null) {
            binding.vm!!.setUserPersonalType()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


}