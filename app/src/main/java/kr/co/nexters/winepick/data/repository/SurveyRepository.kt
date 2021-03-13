package kr.co.nexters.winepick.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.data.source.SearchDataSource
import kr.co.nexters.winepick.data.source.SurveyDataSource

class SurveyRepository(private val surveyDataSource: SurveyDataSource) {
    /** [SurveyDataSource] 를 통해, 설문에 대한 정보 [Survey] 를 가져온다. */
    suspend fun getSurvey() = withContext(Dispatchers.IO) {
        return@withContext surveyDataSource.getSurvey()
    }
}
