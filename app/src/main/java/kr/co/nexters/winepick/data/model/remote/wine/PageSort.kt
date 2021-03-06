package kr.co.nexters.winepick.data.model.remote.wine

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.co.nexters.winepick.network.WinePickService

/**
 * TODO 해당 내용 스웨거에 반영되면 주석 보강 예정
 * [WinePickService.getWines] 의 data.pageable.sort 의 구조
 *
 * @param empty 와인 목록
 * @param sorted
 * @param unsorted
 */
@Serializable
data class PageSort(
    @SerialName("empty")
    @SerializedName("empty")
    val empty: Boolean? = null,

    @SerialName("sorted")
    @SerializedName("sorted")
    val sorted: Boolean? = null,

    @SerialName("unsorted")
    @SerializedName("unsorted")
    val unsorted: Boolean? = null
)
