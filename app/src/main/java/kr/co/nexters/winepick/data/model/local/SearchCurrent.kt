package kr.co.nexters.winepick.data.model.local

import kotlinx.serialization.Serializable
import java.util.*

/**
 * 검색한 내역 data 클래스
 *
 * @param time unixTime
 * @param value 문자열 value
 *
 * @since v1.0.0 / 2021.02.10
 */
@Serializable
data class SearchCurrent(
    val time: Long,
    val value: String
)

fun getSearchCurrentsResponse() = listOf(
    SearchCurrent(0, "a"),
    SearchCurrent(1, "b"),
    SearchCurrent(2, "c"),
    SearchCurrent(3, "d")
)
