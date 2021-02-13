package kr.co.nexters.winepick.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.kakao.auth.Session
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.databinding.ActivityLoginBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.ui.login.getKakaoHashKey
import timber.log.Timber


class HomeActivity : BaseActivity<ActivityHomeBinding>(
        R.layout.activity_home
) {
    override val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private var mode : String = "guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.apply {
            mode = intent!!.getStringExtra("mode").toString()
            if (mode == "user") {
                binding.layoutHomeGuest.visibility = View.INVISIBLE
                binding.clHomeUser.visibility = View.VISIBLE
            }


        }


    }
}