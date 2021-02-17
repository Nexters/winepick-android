package kr.co.nexters.winepick.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.kakao.auth.Session
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.databinding.ActivityLoginBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.ui.login.getKakaoHashKey
import kr.co.nexters.winepick.util.initLoginWarningDialog
import org.koin.android.ext.android.inject
import timber.log.Timber


class HomeActivity : BaseActivity<ActivityHomeBinding>(
        R.layout.activity_home
) {
    override val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }
    private val authManager : AuthManager by inject()

    private var mode : String = "guest"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.apply {
            mode = intent!!.getStringExtra("mode").toString()
            if (mode == "user") {
                if (!authManager.test_type.isNullOrEmpty()) {
                    binding.clHomeUser.visibility = View.VISIBLE
                    binding.layoutHomeGuest.clHomeGuest.visibility = View.INVISIBLE
                }
                binding.layoutHomeGuest.clHomeGuest.visibility = View.VISIBLE
                binding.clLikeNum.visibility = View.VISIBLE
            }

            binding.layoutHomeGuest.clGuestType.setOnClickListener {
                //TODO 테스트 검사하는 창 전환해야함
            }

            binding.clLikeBox.setOnClickListener {
                if (mode == "guest") {

                }
            }




        }


    }

}