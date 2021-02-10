package kr.co.nexters.winepick.data.model.remote.newWine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.co.nexters.winepick.network.WinePickService

/**
 * TODO 해당 내용 스웨거에 반영되면 주석 보강 예정
 * [WinePickService.getWines] 의 data.pageable 의 구조
 *
 * @param offset
 * @param pageNumber
 * @param pageSize
 * @param paged
 * @param sort
 * @param unpaged
 */
@Serializable
data class PageInfo(
    @SerialName("offset")
    val offset: Int,

    @SerialName("pageNumber")
    val pageNumber: Int,

    @SerialName("pageSize")
    val pageSize: Int,

    @SerialName("paged")
    val paged: Boolean?,

    @SerialName("sort")
    val sort: PageSort,

    @SerialName("unpaged")
    val unpaged: Boolean
)
