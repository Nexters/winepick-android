package kr.co.nexters.winepick.data.response

import com.google.gson.annotations.SerializedName

/**
 * 와인 테스트 결과 Data
 */
data class PersonalityType(
    /** A,B,C ... **/
    @SerializedName("personalityType")
    val answerType : String,
    /** 센치한 도도형 **/
    @SerializedName("subDesc")
    val personDetail : String,
    /** 설명 **/
    @SerializedName("description")
    val description : String,
    @SerializedName("keyword1")
    val keyword1 : String,
    @SerializedName("keyword2")
    val keyword2 : String,
    @SerializedName("matchWine")
    val matchWine : String,
    @SerializedName("feature")
    val feature : String,
    @SerializedName("score")
    val score : Int
)