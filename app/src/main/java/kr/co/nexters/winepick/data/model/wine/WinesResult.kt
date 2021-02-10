package kr.co.nexters.winepick.data.model.wine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.data.constant.DependencyServer
import kr.co.nexters.winepick.data.model.WinePickResponse
import kr.co.nexters.winepick.network.WinePickService

/**
 * TODO 해당 내용 스웨거에 반영되면 주석 보강 예정
 * [WinePickService.getWines] response 데이터의 data 구조
 *
 * @param wineResult 와인 목록
 * @param empty
 * @param first
 * @param last
 * @param number
 * @param numberOfElements
 * @param pageable
 * @param size
 * @param sort
 * @param totalElements
 * @param totalPages
 */
@Serializable
@DependencyServer
data class WinesResult(
    @SerialName("content")
    val wineResult: List<WineResult>,

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

fun getWinesResponse(): WinePickResponse<WinesResult> = Json.decodeFromString("""
    {
      "statusCode": 200,
      "message": "0",
      "data": {
        "content": [
          {
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
            "suitFood": "찹스테이크, 양고기",
            "likes": 0
          },
          {
            "id": 2,
            "nmKor": "쁘띠폴리",
            "nmEng": "Petites Folies",
            "country": "프랑스",
            "price": 18000,
            "category": "레드와인",
            "sweetness": 1,
            "acidity": 3,
            "body": 3,
            "tannin": 3,
            "suitFood": "찹스테이크, 양고기",
            "likes": 0
          },
          {
            "id": 3,
            "nmKor": "쁘띠폴리",
            "nmEng": "Petites Folies",
            "country": "프랑스",
            "price": 18000,
            "category": "레드와인",
            "sweetness": 1,
            "acidity": 3,
            "body": 3,
            "tannin": 3,
            "suitFood": "찹스테이크, 고기",
            "likes": 0
          }
        ],
        "pageable": {
          "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
          },
          "offset": 0,
          "pageNumber": 0,
          "pageSize": 20,
          "paged": true,
          "unpaged": false
        },
        "totalPages": 1,
        "totalElements": 3,
        "last": true,
        "size": 20,
        "number": 0,
        "sort": {
          "sorted": false,
          "unsorted": true,
          "empty": true
        },
        "numberOfElements": 3,
        "first": true,
        "empty": false
      }
    }
""".trimIndent())
