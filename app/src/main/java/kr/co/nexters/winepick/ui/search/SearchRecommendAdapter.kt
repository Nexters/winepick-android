package kr.co.nexters.winepick.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.databinding.ItemSearchRecommendBinding
import kr.co.nexters.winepick.ui.base.MultipleSpanHelper
import kr.co.nexters.winepick.util.dpToPx
import kr.co.nexters.winepick.util.pxToDp
import kr.co.nexters.winepick.util.setOnSingleClickListener
import kotlin.properties.Delegates

/**
 * 검색 추천 아이템 recyclerview adapter
 * @see [Create a tagLayout using recyclerView and custom adapter on Android](https://medium.com/@amindevv/create-a-taglayout-using-recyclerview-and-custom-adapter-on-android-bb73b803089d)
 *
 * @since v1.0.0 / 2021.02.08
 */
class SearchRecommendAdapter(
    val vm: SearchViewModel, private var items: Array<String>
) : RecyclerView.Adapter<SearchRecommendViewHolder>(),
    MultipleSpanHelper<SearchRecommendViewHolder> {
    override var measureHelper = MeasureHelper(this, items.size)

    /** recyclerView 가 view 에 붙는 것을 캐치하기 위해 내부에 두는 RecyclerView 변수 */
    override var multipleSpanRecyclerView: RecyclerView? = null

    /** recyclerView 의 너비 측정이 끝났는지 확인하는 flag */
    override var ready: Boolean = false

    /** 모든 아이템의 측정이 끝났는지를 확인하는 flag (해당 값에 값을 설정할 때마다 내부의 익명함수가 실행된다.) */
    var measuringDone by Delegates.observable(false) { _, _, newVal ->
        if (newVal) update()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecommendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchRecommendBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_search_recommend, parent, false)

        return SearchRecommendViewHolder(binding).apply {
            itemView.setOnSingleClickListener {
                vm.querySearchClick(binding.searchRecommend!!, pageNumber = 0)
            }
        }
    }

    /** 새로운 LayoutManager 를 설정해주기 위해 이 함수를 부른다. */
    override fun update() {
        multipleSpanRecyclerView ?: return

        multipleSpanRecyclerView?.apply {
            visibility = View.VISIBLE

            // Get the list of sorted items from measureHelper
            items = measureHelper.getItems()

            // Get the list of sorted spans from measureHelper
            layoutManager = MultipleSpanGridLayoutManager(
                context,
                MeasureHelper.SPAN_COUNT,
                measureHelper.getSpans()
            )
        }
    }

    /**
     * recyclerView 가 연결될 시 너비를 측정하고 measureHelper 에서 baseCell 을 계산한다.
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.multipleSpanRecyclerView = recyclerView

        this.multipleSpanRecyclerView = recyclerView.apply {
            visibility = View.INVISIBLE

            viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // Prevent the multiple calls
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Measure the BaseCell on MeasureHelper.
                    measureHelper.measureBaseCell(recyclerView.width.pxToDp())

                    ready = true

                    // NotifyDataSet because itemCount value needs to update.
                    notifyDataSetChanged()
                }
            })
        }
    }

    override fun onBindViewHolder(holder: SearchRecommendViewHolder, position: Int) {
        val tag = items[position]

        val shouldMeasure = measureHelper.shouldMeasure()

        holder.bind(items[position], vm)

        if (!shouldMeasure) {
            val title = holder.itemView.findViewById<TextView>(R.id.tv_search_recommend_title)
            title.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        } else if (shouldMeasure)
            measure(holder, tag)
    }

    override fun getItemCount(): Int = items.size

    /** 마지막 아이템인지 확인한다. */
    override fun cellMeasured() {
        if (!measuringDone && !measureHelper.shouldMeasure())
            measuringDone = true
    }

    override fun measure(holder: RecyclerView.ViewHolder, tag: String) {
        /* Get the ItemView and minimize it's height as small as possible
            to fit as many cells as it's possible in the screen. */

        val itemView = holder.itemView.apply { layoutParams.height = 42.dpToPx() }

        val globalLayoutListener = object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // OnGlobalLayoutListener 리스너 중복 방지를 위해 viewTreeObserver 에 등록된 리스너를 제거한다.
                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // RecyclerView 내에서 아이템뷰가 차지하는 % 를 구한다 (소수점 값)
                val title = itemView.findViewById<TextView>(R.id.tv_search_recommend_title)
                val span = (title.width.pxToDp()) / measureHelper.baseCell

                // 새로운 View 가 들어갈 것임을 알린다.
                measureHelper.addMeasuredCount()

                // rowManager 에 해당 아이템을 더한다.
                measureHelper.rowManager.add(span, tag)

                // 마지막 아이템인지 확인한다.
                cellMeasured()
            }
        }

        // Observe for the view
        itemView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }
}

class SearchRecommendViewHolder(private val binding: ItemSearchRecommendBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(searchRecommand: String, vm: SearchViewModel) {
        binding.searchRecommend = searchRecommand
        binding.vm = vm

        binding.executePendingBindings()
    }
}
