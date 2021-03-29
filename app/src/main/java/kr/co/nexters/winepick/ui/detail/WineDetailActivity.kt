package kr.co.nexters.winepick.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.databinding.ActivityHomeBinding
import kr.co.nexters.winepick.databinding.ActivityWineDetailBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.component.ConfirmDialog
import kr.co.nexters.winepick.ui.component.LikeDialog
import kr.co.nexters.winepick.ui.home.HomeViewModel
import kr.co.nexters.winepick.ui.login.LoginViewModel
import kr.co.nexters.winepick.util.drawCancelToast
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class WineDetailActivity : BaseActivity<ActivityWineDetailBinding>(R.layout.activity_wine_detail) {
    override val viewModel: WineDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val wineResult : WineResult = intent.getSerializableExtra("wineData") as WineResult

        viewModel.getWineResult(wineResult)
        binding.setVariable(BR.vm, viewModel)

        viewModel.toastMessage.observe(this, Observer {
            if(it) {
                LikeDialog(
                        content = getString(R.string.like_add)
                ).show(supportFragmentManager, "LikeDialog")

            }
        })

        viewModel.cancelMessage.observe(this, Observer {
            if(it) {
                val customToast = Toast(this)
                customToast.drawCancelToast(this)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
