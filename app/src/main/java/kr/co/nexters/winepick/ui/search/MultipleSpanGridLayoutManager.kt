package kr.co.nexters.winepick.ui.search

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

/**
 * 한 줄에 가변적인 아이템이 들어갈 수 있는 GridLayoutManager
 *
 * @since v1.0.0 / 2021.02.17
 */
class MultipleSpanGridLayoutManager(
    context: Context, spanCount: Int, spanList: Array<Int>
) : GridLayoutManager(context, spanCount) {
    init {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int) = spanList[position]
        }
    }
}
