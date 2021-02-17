package kr.co.nexters.winepick.data.model.remote.wine

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.data.constant.DependencyServer
import kr.co.nexters.winepick.data.model.WinePickResponse
import kr.co.nexters.winepick.network.WinePickService

/**
 * TODO 해당 내용 스웨거에 반영되면 주석 보강 예정
 * [WinePickService.getWine] response 데이터의 data 구조
 *
 * @param acidity
 * @param body
 * @param category
 * @param country
 * @param feeling
 * @param id
 * @param likes
 * @param nmEng
 * @param nmKor
 * @param price
 * @param suitEvent
 * @param suitFood
 * @param suitWho
 * @param sweetness
 * @param tannin
 */
@Serializable
data class WineResult(
    @SerializedName("acidity")
    @SerialName("acidity")
    val acidity: Int,

    @SerializedName("body")
    @SerialName("body")
    val body: Int,

    @SerializedName("category")
    @SerialName("category")
    val category: String,

    @SerializedName("country")
    @SerialName("country")
    val country: String,

    @SerializedName("felling")
    @SerialName("feeling")
    val feeling: String,

    @SerializedName("id")
    @SerialName("id")
    val id: Int,

    @SerializedName("likes")
    @SerialName("likes")
    val likes: Int,

    @SerializedName("nmEng")
    @SerialName("nmEng")
    val nmEng: String,

    @SerializedName("nmKor")
    @SerialName("nmKor")
    val nmKor: String,

    @SerializedName("price")
    @SerialName("price")
    val price: Int,

    @SerializedName("suitEvent")
    @SerialName("suitEvent")
    val suitEvent: String?,

    @SerializedName("suitFood")
    @SerialName("suitFood")
    val suitFood: String?,

    @SerializedName("suitWho")
    @SerialName("suitWho")
    val suitWho: String?,

    @SerializedName("sweetness")
    @SerialName("sweetness")
    val sweetness: Int,

    @SerializedName("tannin")
    @SerialName("tannin")
    val tannin: Int
)

fun getWineResponse(): WinePickResponse<WineResult> = Json.decodeFromString(
    """
    {
      "statusCode": 200,
      "message": "0",
      "data": {
        "id": 1,
        "nmKor": "쁘띠폴리",
        "nmEng": "Petites Folies",
        "country": "프랑스",
        "price": 18000,
        "category": "레드와인",
        "sweetness": 1,
        "acidity": 3,
        "body": 3,
        "tannin": 3,
        "feeling": "드라이한 화이트지만, 달달하게 잘 익은 배의 풍미로 시작되어 크리미한 질감까지 블라인딩 와인의 장점을 살린 와인",
        "suitWho": null,
        "suitEvent": null,
        "suitFood": "찹스테이크, 양고기",
        "likes": 0
      }
    }
""".trimIndent()
)
