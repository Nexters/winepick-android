package kr.co.nexters.winepick.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleObserver
import kr.co.nexters.winepick.data.model.local.SearchFilterItem
import kr.co.nexters.winepick.databinding.LayoutSearchFilterItemBinding
import kr.co.nexters.winepick.util.setOnSingleClickListener
import timber.log.Timber

/**
 * 검색 필터 설정화면에서 사용하는 아이템 뷰
 *
 * @since v1.0.0 / 2021.02.07
 */
class SearchFilterItemButton(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs) {
    private val activity: SearchFilterActivity get() = this.context as SearchFilterActivity
    private var binding = LayoutSearchFilterItemBinding.inflate(
        LayoutInflater.from(context), this.rootView as? ViewGroup, false
    )
    val root = binding.root as AppCompatTextView

    fun bind(viewModel: SearchFilterViewModel?, searchFilterItem: SearchFilterItem) {
        binding.vm = viewModel
        binding.searchFilterItem = searchFilterItem
        binding.lifecycleOwner = activity

        binding.executePendingBindings()
    }
}
