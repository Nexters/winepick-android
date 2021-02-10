package kr.co.nexters.winepick.data.model.remote.newWine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO 해당 내용 스웨거에 반영되면 주석 추가 예정
@Serializable
data class WineResult(
    @SerialName("acidity")
    val acidity: Int,

    @SerialName("body")
    val body: Int,

    @SerialName("category")
    val category: String,

    @SerialName("country")
    val country: String,

    @SerialName("feeling")
    val feeling: String,

    @SerialName("id")
    val id: Int,

    @SerialName("likes")
    val likes: Int,

    @SerialName("nmEng")
    val nmEng: String,

    @SerialName("nmKor")
    val nmKor: String,

    @SerialName("price")
    val price: Int,

    @SerialName("suitEvent")
    val suitEvent: String?,

    @SerialName("suitFood")
    val suitFood: String?,

    @SerialName("suitWho")
    val suitWho: String?,

    @SerialName("sweetness")
    val sweetness: Int,

    @SerialName("tannin")
    val tannin: Int
)
