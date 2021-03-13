package kr.co.nexters.winepick.ui.search

import androidx.recyclerview.widget.RecyclerView

/**
 * [MultipleSpanGridLayoutManager] 에서 제대로 길이를 측정할 수 있도록 도와주는 Helper
 *
 * @param adapter [MeasureHelper] 를 적용시킬 RecyclerViewAdapter
 * @param count 측정될 아이템의 개수
 *
 * @since v1.0.0 / 2021.02.17
 */
class MeasureHelper<VH : RecyclerView.ViewHolder>(
    private val adapter: RecyclerView.Adapter<VH>,
    private val count: Int
) {
    companion object {
        const val SPAN_COUNT = 52
    }

    /** 측정한 View 의 수. 측정 완료시기를 캐치하기 위해 사용 */
    private var measuredCount = 0

    /** TagRow 를 관리하는 Manager */
    val rowManager = TagRowManager()

    /** viewHolder 가 얼마만큼의 span 이 필요로 하는지에 대한 값을 결정한다. */
    var baseCell: Float = 0f

    fun measureBaseCell(widthDp: Int) {
        baseCell = (widthDp / SPAN_COUNT).toFloat()
    }

    fun addMeasuredCount() {
        measuredCount++
    }

    /** 측정이 모두 끝났는지 안 끝났는지 확인하는 flag 값 */
    fun shouldMeasure() = measuredCount != count

    fun getItems(): Array<String> = rowManager.getSortedTags().toTypedArray()

    fun getSpans(): Array<Int> = rowManager.getSortedSpans().toTypedArray()
}
