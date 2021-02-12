package kr.co.nexters.winepick.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kr.co.nexters.winepick.BR
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.model.local.SearchFilterCategory
import kr.co.nexters.winepick.data.model.local.SearchFilterGroup
import kr.co.nexters.winepick.data.model.local.SearchFilterItem
import kr.co.nexters.winepick.databinding.ActivitySearchFilterBinding
import kr.co.nexters.winepick.ui.base.BaseActivity
import kr.co.nexters.winepick.util.dpToPx
import kr.co.nexters.winepick.util.setOnSingleClickListener

/**
 * 검색 필터 변경 화면
 *
 * @since v1.0.0 / 2021.02.11
 */
class SearchFilterActivity : BaseActivity<ActivitySearchFilterBinding>(
    R.layout.activity_search_filter
) {
    override val viewModel: SearchFilterViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SearchFilterViewModel::class.java)
    }

    val searchFilterItemMap = mutableMapOf<AppCompatTextView, SearchFilterItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        binding.apply {

        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.searchFilterItems.observe(this, { generateFilterItems(it) })
        viewModel.changeSearchFilterItem.observe(this, { newSearchFilterItem ->
            val view = searchFilterItemMap.keys
                .filter { searchFilterItemMap[it]?.id == newSearchFilterItem.id }[0]

            searchFilterItemMap[view] = newSearchFilterItem
            view.changeSearchFilterItemUI(newSearchFilterItem)
        })
    }

    private fun generateFilterItems(searchFilterItems: List<SearchFilterItem>) = uiScope.launch {
        val groupingItems = searchFilterItems.groupBy { it.category }

        val tastes = groupingItems[SearchFilterCategory.TASTE]

        val contents = tastes?.filter { it.group == SearchFilterGroup.CONTENT }
        // seekbar 세팅

        val tasteTypes = tastes?.filter { it.group == SearchFilterGroup.TYPE }
        binding.layoutSearchFilterTasteType.addFilterItems(tasteTypes, SearchFilterGroup.TYPE)

        val tasteTastes = tastes?.filter { it.group == SearchFilterGroup.TASTE }
        binding.layoutSearchFilterTasteTaste.addFilterItems(
            tasteTastes,
            SearchFilterGroup.TASTE
        )

        val situations = groupingItems[SearchFilterCategory.SITUATION]

        val situationEvents = situations?.filter { it.group == SearchFilterGroup.EVENT }
        binding.layoutSearchFilterSituationEvent.addFilterItems(
            situationEvents,
            SearchFilterGroup.EVENT
        )

        val situationFoods = situations?.filter { it.group == SearchFilterGroup.FOOD }
        binding.layoutSearchFilterSituationFood.addFilterItems(
            situationFoods,
            SearchFilterGroup.FOOD
        )

        val wheres = groupingItems[SearchFilterCategory.WHERE]

        val whereConveniences = wheres?.filter { it.group == SearchFilterGroup.CONVENIENCE }
        binding.layoutSearchFilterWhereConvenience.addFilterItems(
            whereConveniences,
            SearchFilterGroup.CONVENIENCE
        )
    }

    private fun LinearLayout.addFilterItems(
        tastes: List<SearchFilterItem>?, group: SearchFilterGroup
    ) {
        this.post {
            uiScope.launch {
                val maxWidth = this@addFilterItems.width
                var count = 0

                val searchFilterTypes = tastes?.filter { it.group == group }?.toMutableList()
                while (!searchFilterTypes.isNullOrEmpty()) {
                    val childLinearLayout = LinearLayout(this@SearchFilterActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply { bottomMargin = 8.dpToPx() }
                        orientation = LinearLayout.HORIZONTAL
                    }

                    var childButton: SearchFilterItemButton? = null
                    do {
                        childButton?.root?.let { it ->
                            it.id = View.generateViewId()
                            it.setOnSingleClickListener {
                                viewModel.searchFilterItemClick(searchFilterItemMap[it]!!)
                            }

                            // 자식뷰를 더하고 list 에서 제거한다.
                            searchFilterItemMap[it] = searchFilterTypes.first()
                            childLinearLayout.addView(it)
                                .apply { searchFilterTypes.removeFirst() }

                            // 공백 칸을 더해준다.
                            childLinearLayout.addView(View(this@SearchFilterActivity).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    8.dpToPx(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                            })
                            count += 8.dpToPx()
                        }

                        // 비었다면 더 이상 진행하지 않는다.
                        if (searchFilterTypes.isEmpty()) break

                        // 자식 뷰를 만든다.
                        childButton = SearchFilterItemButton(this@SearchFilterActivity)
                        childButton.bind(viewModel, searchFilterTypes.first())

                        // 측정하여 예상치를 계산한다.
                        childButton.root.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                        count += (childButton.root.measuredWidth)
                    } while (count < maxWidth)

                    count = 0

                    this@addFilterItems.addView(childLinearLayout)
                }
            }
        }
    }

    override fun onBackPressed() {
        setResult(
            Constant.REQ_CODE_GO_TO_FILTER,
            Intent().apply { putExtra(Constant.INT_EXTRA_FILTER_NUM, 3) })
        super.onBackPressed()
    }

    private fun AppCompatTextView.changeSearchFilterItemUI(searchFilterItem: SearchFilterItem) {
        this.isSelected = searchFilterItem.selected
        if (searchFilterItem.selected) {
            setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            setTextColor(ContextCompat.getColor(context, R.color.search_filter_item_text_color))
        }
    }
}

