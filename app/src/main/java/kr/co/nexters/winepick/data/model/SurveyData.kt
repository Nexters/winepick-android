package kr.co.nexters.winepick.data.model

data class SurveyData(
    val answers: List<SurveyAnswers>,
    val content: String,
    val createAt: String,
    val id: Int,
    val updateAt: String
)
