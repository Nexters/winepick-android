package kr.co.nexters.winepick.ui.type

import android.os.Bundle
import android.view.View
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ActivityTypeDetailBinding
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.type.RecentSearchAdapter
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.util.VerticalItemDecorator
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeDetailActivity : BaseActivity<ActivityTypeDetailBinding>(
    R.layout.activity_type_detail
) {
    override val viewModel : TypeDetailModel by viewModel<TypeDetailModel>()
    private val authManager : AuthManager by inject()
    private val searchRecycler : RecentSearchAdapter by lazy { RecentSearchAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)
        binding.vm!!.setUserPersonalType()
        viewModel.backButton.observe(this, {
            if (it) finish()
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
               //TODO SearchActivity 검색 처리해야함  !
                startActivity(SearchActivity::class, false)
            }
        })
    }

}
