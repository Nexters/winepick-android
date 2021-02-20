package kr.co.nexters.winepick.ui.base

import androidx.recyclerview.widget.RecyclerView
import kr.co.nexters.winepick.ui.search.MeasureHelper

/**
 * 각 행마다 가변적인 아이템 개수가 들어가는 경우 implement 해주어야 하는 interface
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.17
 */
interface MultipleSpanHelper<VH : RecyclerView.ViewHolder> {
    var measureHelper: MeasureHelper<VH>

    /** recyclerView 가 view 에 붙는 것을 캐치하기 위해 내부에 두는 RecyclerView 변수 */
    var multipleSpanRecyclerView: RecyclerView?

    /** recyclerView 의 너비 측정이 끝났는지 확인하는 flag */
    var ready: Boolean

    /** 모든 아이템들의 측정이 끝났을 떄 실행해주는 로직이다. */
    fun update()

    /**
     * 아이템이 측정될 때마다 이 함수를 호출하며,
     * 측정이 다 되었을 시 내부에서 update 를 호출해주어야 한다.
     */
    fun cellMeasured()

    /**
     * 해당 아이템의 너비를 측정하고 TagRow 에 추가하는 로직
     * itemView 의 높이를 알수 있게 될 때마다 실행된다.
     */
    fun measure(holder: RecyclerView.ViewHolder, tag: String)
}
