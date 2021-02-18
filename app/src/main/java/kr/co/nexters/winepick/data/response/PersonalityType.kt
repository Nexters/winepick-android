package kr.co.nexters.winepick.data.response

import com.google.gson.annotations.SerializedName

data class PersonalityType(
    /** A,B,C ... **/
    @SerializedName("answerType")
    val answerType : String,
    /** 고양이,강아지, ... **/
    @SerializedName("personality")
    val personName : String,
    @SerializedName("subDesc")
    val personDetail : String,
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