package kr.co.nexters.winepick.ui.type

import android.os.Bundle
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityTypeDetailBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeDetailActivity : BaseActivity<ActivityTypeDetailBinding>(
    R.layout.activity_type_detail
) {
    override val viewModel : TypeDetailModel by viewModel<TypeDetailModel>()
    private val authManager : AuthManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.vm!!.setUserPersonalType()
        binding.apply {
            ivTypeBack.setOnClickListener { finish() }
        }



    }

}