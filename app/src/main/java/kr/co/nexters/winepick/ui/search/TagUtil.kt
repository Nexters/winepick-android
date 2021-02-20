package kr.co.nexters.winepick.ui.search

/**
 * 태그형 RecyclerView 에서 사용되는 유틸 및 데이터 클래스
 *
 * @since v1.0.0 / 2021.02.18
 */

/**
 * 각 행마다 들어가는 데이터 모임
 *
 * @param freeSpans 각 행별 최대로 들어갈 수 있는 개수
 * @param tagList 현재 TagRow 에서 가지고 있는 아이템 목록
 * @param spanList 현재 TagRow 에서 가지고 있는 아이템의 Span 목록
 */
data class TagRow(
    var freeSpans: Int = MeasureHelper.SPAN_COUNT,
    val tagList: MutableList<String> = mutableListOf(),
    val spanList: MutableList<Int> = mutableListOf()
) {
    fun addItem(spanRequired: Float, item: String): Boolean {
        // if the current row has enough available span
        if (spanRequired < freeSpans)
            if (tagList.add(item)) {
                // Round the required span to Int
                val spanRequiredInt = kotlin.math.ceil(spanRequired.toDouble()).toInt()

                // Add the required span to spanList
                spanList.add(spanRequiredInt)

                // Minus the required span from the available span.
                freeSpans -= spanRequiredInt

                return true
            }
        return false
    }
}

/** [TagRow] 를 관리하는 Manager */
class TagRowManager {
    /** 각 row 별 아이템 정보 */
    private val rows = mutableListOf<TagRow>()
        // Manually add the first empty row.
        .apply { add(TagRow()) }

    /** spanRequired 에 만족한 경우 tagRow 에 값을 넣고, 아닐 경우 만족할때까지 반복한다. */
    fun add(spanRequired: Float, value: String?) {
        value?.let {
            for (i in 0 until rows.size) {
                val tagRow = rows[i]

                // tag row 에 성공적으로 들어갔을 시 return 시킨다.
                if (tagRow.addItem(spanRequired, value))
                    break

                // 더 이상 tag row 에 들어갈 수 없다면 새로운 tag row 를 만들고 재귀로 호출한다.
                if (i == rows.lastIndex) {
                    rows.add(TagRow())
                    add(spanRequired, value)
                }
            }
        }
    }

    /** spanList 를 리턴한다. */
    fun getSortedSpans(): List<Int> = mutableListOf<Int>().apply {
        rows.forEach { addAll(it.spanList) }
    }

    /** tagList 를 리턴한다. */
    fun getSortedTags(): List<String> = mutableListOf<String>().apply {
        rows.forEach {
            addAll(it.tagList)
        }
    }
}

