package kr.co.nexters.winepick.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
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

        ConfirmDialog(
                title = getString(R.string.login_warning_title),
                content = getString(R.string.login_warning_like),
                leftText = getString(R.string.login_warning_btn_left_text),
                leftClickListener = {
                    Toast.makeText(this, "왼쪽 버튼을 클릭한다.", Toast.LENGTH_SHORT).show()
                },
                rightText = getString(R.string.login_warning_btn_right_text),
                rightClickListener = {
                    Toast.makeText(this, "오른쪽 버튼을 클릭한다.", Toast.LENGTH_SHORT).show()
                    it.dismiss()
                },
                cancelable = false
        ).show(supportFragmentManager, "LoginWarningDialog")
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


}
