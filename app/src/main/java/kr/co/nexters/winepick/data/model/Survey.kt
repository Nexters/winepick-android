package kr.co.nexters.winepick.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** 서버에서 받아오는 설문 데이터 */
@Serializable
data class Survey(
    @SerializedName("createdAt")
    @SerialName("createdAt")
    val createAt: String? = "",

    @SerializedName("updatedAt")
    @SerialName("updatedAt")
    val updateAt: String? = "",

    @SerializedName("id")
    @SerialName("id")
    val id: Int? = -1,

    @SerializedName("content")
    @SerialName("content")
    val content: String? = "",

    @SerializedName("answerA")
    @SerialName("answerA")
    val answersA: String? = "",

    @SerializedName("answerB")
    @SerialName("answerB")
    val answersB: String? = "",

    @SerializedName("selectedAnswer")
    @SerialName("selectedAnswer")
    val selectedAnswer: SurveyAnswerType = SurveyAnswerType.UNKNOWN
)

enum class SurveyAnswerType {
    UNKNOWN,
    ANSWER_A,
    ANSWER_B,
}

/** 사람의 성향을 구분하는 enum class */
enum class PersonVersion {
    UNKNOWN,
    INTROVERSION,
    EXTROVERSION,
}

/** 응답 결과를 문자열로 파싱한다. */
fun SurveyAnswerType.parse() = when (this) {
    SurveyAnswerType.UNKNOWN -> ""
    SurveyAnswerType.ANSWER_A -> "A"
    SurveyAnswerType.ANSWER_B -> "B"
}

data class SurveyInfo(
    val number: Int,
    val survey: Survey
)
