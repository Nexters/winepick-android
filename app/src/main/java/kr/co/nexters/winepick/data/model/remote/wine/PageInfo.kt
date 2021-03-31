package kr.co.nexters.winepick.data.model.remote.wine

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.co.nexters.winepick.data.constant.DependencyServer
import kr.co.nexters.winepick.network.WinePickService

/**
 * TODO 해당 내용 스웨거에 반영되면 주석 보강 예정
 * [WinePickService.getWines] 의 data.pageable 의 구조
 *
 * @param offset
 * @param page
 * @param size
 * @param paged
 * @param sort
 * @param unpaged
 */
@Serializable
@DependencyServer
data class PageInfo(
    @SerialName("offset")
    @SerializedName("offset")
    val offset: Int? = null,

    @SerialName("page")
    @SerializedName("page")
    val page: Int? = null,

    @SerialName("size")
    @SerializedName("size")
    val size: Int? = null,

    @SerialName("paged")
    @SerializedName("paged")
    val paged: Boolean? = null,

    @SerialName("sort")
    @SerializedName("sort")
    val sort: PageSort? = null,

    @SerialName("unpaged")
    @SerializedName("unpaged")
    val unpaged: Boolean? = null
)
