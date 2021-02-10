package kr.co.nexters.winepick.data.model.wine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.co.nexters.winepick.data.constant.DependencyServer
import kr.co.nexters.winepick.network.WinePickService

/**
 * TODO 해당 내용 스웨거에 반영되면 주석 보강 예정
 * [WinePickService.getWines] 의 data.sort 의 구조
 *
 * @param empty 와인 목록
 * @param sorted
 * @param unsorted
 */
@Serializable
@DependencyServer
data class Sort(
    @SerialName("empty")
    val empty: Boolean,

    @SerialName("sorted")
    val sorted: Boolean,

    @SerialName("unsorted")
    val unsorted: Boolean
)
