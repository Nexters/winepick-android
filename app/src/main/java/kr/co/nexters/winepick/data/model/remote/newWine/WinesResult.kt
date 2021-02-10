package kr.co.nexters.winepick.data.model.remote.newWine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.co.nexters.winepick.data.constant.DependencyServer

// TODO 해당 내용 스웨거에 반영되면 주석 추가 예정
@Serializable
@DependencyServer
data class WinesResult(
    @SerialName("content")
    val content: List<WineResult>,

    @SerialName("empty")
    val empty: Boolean,

    @SerialName("first")
    val first: Boolean,

    @SerialName("last")
    val last: Boolean,

    @SerialName("number")
    val number: Int,

    @SerialName("numberOfElements")
    val numberOfElements: Int,

    @SerialName("pageable")
    val pageable: PageInfo,

    @SerialName("size")
    val size: Int,

    @SerialName("sort")
    val sort: Sort,

    @SerialName("totalElements")
    val totalElements: Int,

    @SerialName("totalPages")
    val totalPages: Int
)
