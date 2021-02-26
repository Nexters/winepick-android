package kr.co.nexters.winepick.data.response

import com.google.gson.annotations.SerializedName
import kr.co.nexters.winepick.data.model.Survey

data class SurveyResponse (
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("content")
    val question: String? = null,
    @SerializedName("answerA")
    val answerA: String? = null,
    @SerializedName("answerB")
    val answerB: String? = null
)