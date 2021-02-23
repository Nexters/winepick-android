package kr.co.nexters.winepick.data.model

data class Survey(
    val data: List<SurveyData>,
    val message: String,
    val statusCode: Int
)
