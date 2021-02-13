package kr.co.nexters.winepick.ui.search

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.launch
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
import timber.log.Timber


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

    // (터치하는 텍스트 버튼 -> 데이터) Map
    private val searchFilterItemMap = mutableMapOf<AppCompatTextView, SearchFilterItem>()

    // 도수 Range Slide 에서 사용하는 (도수 숫자 -> 텍스트뷰) Map
    // TODO Range Slider 마개조하면서 활용할 예정
    private val searchFilterContentTexts = mutableMapOf<String, AppCompatTextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(BR.vm, viewModel)

        binding.apply {
            btnSearchFilterBack.setOnSingleClickListener { onBackPressedAtSearchFilter() }
            btnSearchFilterConfirmBottom.setOnSingleClickListener { onBackPressedAtSearchFilter(true) }
            btnSearchFilterConfirm.setOnSingleClickListener { onBackPressedAtSearchFilter(true) }
        }

        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.prevFilterItems.observe(this, { generateFilterItems(it) })
        viewModel.changedFilterItem.observe(this, { newSearchFilterItem ->
            // 도수인 경우 별도의 갱신 작업을 진행하지 않는다.
            if (newSearchFilterItem.group == SearchFilterGroup.CONTENT) return@observe

            val view = searchFilterItemMap.keys
                .filter { searchFilterItemMap[it]?.id == newSearchFilterItem.id }[0]

            searchFilterItemMap[view] = newSearchFilterItem
            view.changeSearchFilterItemUI(newSearchFilterItem)
        })
    }

    private fun generateFilterItems(searchFilterItems: List<SearchFilterItem>) = uiScope.launch {
        val groupingItems = searchFilterItems.groupBy { it.category }

        val tastes = groupingItems[SearchFilterCategory.TASTE]

        // seekbar 세팅
        val contents = tastes?.filter { it.group == SearchFilterGroup.CONTENT }
        initContentsWithTexts(contents)

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

    /**
     * [SearchFilterActivity] 에서 화면을 이탈하려 할 때에는 반드시 이걸 사용해야 한다.
     *
     * @param needToCheck 필터 아이템 변동 여부 확인을 해야할 시 (ex. 적용하기 버튼 누를 시)
     */
    fun onBackPressedAtSearchFilter(needToCheck: Boolean = false) {
        setResult(
            Constant.REQ_CODE_GO_TO_FILTER,
            Intent().apply {
                putExtra(
                    Constant.BOOL_EXTRA_SEARCH_NEED_UPDATE,
                    viewModel.checkFilterItemChanged(needToCheck)
                )
            }
        )

        super.onBackPressed()
    }

    /** [onBackPressedAtSearchFilter] 을 강제로 실행하도록 한다. */
    override fun onBackPressed() {
        onBackPressedAtSearchFilter(false)
    }

    /**
     * [검색 필터 아이템 데이터][searchFilterItem] 를 기반으로
     * [검색 필터 설정화면에서 사용하는 아이템 뷰][SearchFilterItemButton] UI 를 수정한다.
     */
    private fun AppCompatTextView.changeSearchFilterItemUI(searchFilterItem: SearchFilterItem) {
        this.isSelected = searchFilterItem.selected
        if (searchFilterItem.selected) {
            setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            setTextColor(ContextCompat.getColor(context, R.color.search_filter_item_text_color))
        }
    }

    /**
     * RangeSlider 를 만들고 리스너를 등록한다.
     * TODO [특정 도수 textView 목록][searchFilterContentTexts] 도 만든다.
     *
     * @see [RangeSlider 머터리얼 디자인 가이드](https://material.io/components/sliders/android#discrete-slider)
     * @see RangeSlider.OnSliderTouchListener
     */
    private fun initContentsWithTexts(contents: List<SearchFilterItem>?) {
        // 발생할 일 없는 에러이며 만약 2개가 아닐 경우 Exception 발생시키기
        if (contents?.size != 2) throw Exception("slider 에는 기본 최소 최대값이 있어야 합니다.")

        val firstValue = contents.first().value.toFloat()
        val lastValue = contents.last().value.toFloat()
        binding.sbSearchFilterTasteContent.values = listOf(firstValue, lastValue)

//        // TODO Range Slider 마개조하면서 활성화시킬 예정
//        val mConstraintSet = ConstraintSet()
//        mConstraintSet.clone(binding.layoutSearchFilterTasteContentTexts)
//
//        // 각 value 마다 textView 만들기
//        for (value in 4..13) {
//            val contentTextView = inflate(
//                this@SearchFilterActivity,
//                R.layout.layout_search_filter_content_text,
//                null
//            ) as AppCompatTextView
//            contentTextView.id = View.generateViewId()
//            contentTextView.text = "$value"
//            searchFilterContentTexts["$value"] = contentTextView
//
//            stylingContentText(firstValue, lastValue, value)
//
//            binding.layoutSearchFilterTasteContentTexts.addView(contentTextView)
//        }
//
//        // 밑에 text 정렬하기 (constraint chain = spread_inside)
//        for (value in 4..13) {
//            val currentView = searchFilterContentTexts["$value"]!!
//            when (value) {
//                4 -> {
//                    val nextView = searchFilterContentTexts["${value + 1}"]
//                    // 먼저 왼쪽에 붙임
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.LEFT,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.LEFT
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.RIGHT,
//                        nextView!!.id,
//                        ConstraintSet.LEFT
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.TOP,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.TOP
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.BOTTOM,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.BOTTOM
//                    )
//                }
//
//                13 -> {
//                    val prevView = searchFilterContentTexts["${value - 1}"]
//                    // 먼저 왼쪽에 붙임
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.RIGHT,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.RIGHT
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.LEFT,
//                        prevView!!.id,
//                        ConstraintSet.RIGHT
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.TOP,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.TOP
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.BOTTOM,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.BOTTOM
//                    )
//                }
//
//                else -> {
//                    val leftView = searchFilterContentTexts["${value - 1}"]
//                    val rightView = searchFilterContentTexts["${value + 1}"]
//                    // 왼쪽 설정
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.LEFT,
//                        leftView!!.id,
//                        ConstraintSet.RIGHT
//                    )
//                    // 오른쪽 설정
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.RIGHT,
//                        rightView!!.id,
//                        ConstraintSet.LEFT
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.TOP,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.TOP
//                    )
//                    mConstraintSet.connect(
//                        currentView.id,
//                        ConstraintSet.BOTTOM,
//                        ConstraintSet.PARENT_ID,
//                        ConstraintSet.BOTTOM
//                    )
//                }
//            }
//
//            mConstraintSet.constrainWidth(currentView.id, WRAP_CONTENT)
//            mConstraintSet.setHorizontalChainStyle(currentView.id, CHAIN_SPREAD_INSIDE)
//        }
//
//        // constraint layout 에 constraitset 반영
//        mConstraintSet.applyTo(binding.layoutSearchFilterTasteContentTexts)

        // Range Slider 리스너 등록
        binding.sbSearchFilterTasteContent.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
            /** 터치 시작 지점 캐치 */
            override fun onStartTrackingTouch(slider: RangeSlider) {
                val values = binding.sbSearchFilterTasteContent.values
                Timber.d("onStartTrackingTouch From ${values[0]}")
                Timber.d("onStartTrackingTouch T0 ${values[1]}")
            }

            /** 터치 종료 지점 캐치 */
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = binding.sbSearchFilterTasteContent.values
                Timber.d("onStopTrackingTouch From ${values[0]}")
                Timber.d("onStopTrackingTouch T0 ${values[1]}")

                viewModel.replaceFilterItem(contents.first(), "${values[0].toInt()}")
                viewModel.replaceFilterItem(contents.last(), "${values[1].toInt()}")

//                // TODO Range Slider 마개조하면서 활성화시킬 예정
//                for(index in 4..13){
//                    stylingContentText(values[0], values[1], index)
//                }
            }
        })
    }

    /** [특정 도수 text 값][value] 에 대해서 텍스트 스타일링을 조정한다. */
    private fun stylingContentText(firstValue: Float, lastValue: Float, value: Int) {
        val contentTextView = searchFilterContentTexts["$value"]
        if (value < firstValue || value > lastValue) {
            contentTextView?.setTextColor(
                ContextCompat.getColor(
                    this@SearchFilterActivity,
                    R.color.search_filter_tastes_content_track_inactive
                )
            )
            contentTextView?.typeface = Typeface.DEFAULT
        } else if (value >= firstValue && value <= lastValue) {
            contentTextView?.setTextColor(
                ContextCompat.getColor(
                    this@SearchFilterActivity,
                    R.color.search_filter_tastes_content_track_active
                )
            )
            contentTextView?.typeface = Typeface.DEFAULT_BOLD
        }
    }
}
