package kr.co.nexters.winepick.data.response

import com.google.gson.annotations.SerializedName
import kr.co.nexters.winepick.data.model.Survey

data class SurveyResponse (
    @SerializedName("content")
    val question: String? = null,
    @SerializedName("answers")
    val answers: Int? = null,
    @SerializedName("statusCode")
    val statusCode: Int? = null
)