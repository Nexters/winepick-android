package kr.co.nexters.winepick.data.model

data class SurveyData(
    val createAt: String,
    val updateAt: String,
    val id: Int,
    val content: String,
    val answersA: String,
    val answersB: String
)
