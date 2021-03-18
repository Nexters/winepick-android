package kr.co.nexters.winepick.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.data.model.SurveyAnswerType
import kr.co.nexters.winepick.data.model.SurveyInfo
import kr.co.nexters.winepick.data.source.SurveyDataSource
import kr.co.nexters.winepick.util.SharedPrefs

class SurveyRepository(
    private val surveyDataSource: SurveyDataSource,
    private val sharedPrefs: SharedPrefs
) {
    /** 설문 목록 */
    private var surveys: List<Survey> = listOf()

    /** 현재 유저 */
    private var cursor: Int = 0

    /** 처음 앱 실행 시, 앱 내에 저장되어 있는 설문 목록울 가져온다. */
    suspend fun load() {
        val inMemorySurveys = Json.decodeFromString<List<Survey>>(
            sharedPrefs[Constant.PREF_KEY_USER_SURVEYS, ""] ?: ""
        )

        surveys = inMemorySurveys
        cursor = findSurveyCursor()
    }

    /** 새로운 설문 정보를 가져온다. */
    suspend fun resetSurvey() = withContext(Dispatchers.IO) {
        val response = surveyDataSource.getSurvey()

        val newSurveys = response?.result ?: listOf()
        saveSurveys(newSurveys)

        surveys = newSurveys
        cursor = 0
    }

    /** 현재 풀어야 하는 인덱스 및 설문 정보를 가져온다. */
    fun getCurrentSurvey(): SurveyInfo? = if (cursor != surveys.size)
        SurveyInfo(cursor, surveys[cursor])
    else
        null

    /**
     * 앱 내에 새롭게 설문한 내용을 저장한다.
     *
     * @param surveys 새롭게 저장할 설문 목록
     */
    private suspend fun saveSurveys(surveys: List<Survey>) = withContext(Dispatchers.IO) {
        val jsonString = Json.encodeToString(surveys)

        sharedPrefs[Constant.PREF_KEY_USER_SURVEYS] = jsonString

        return@withContext
    }

    /** 앱 내에 저장된 설문 목록을 지우고 싶을 시 호출한다. */
    suspend fun clearSurveys() = withContext(Dispatchers.IO) {
        surveys = listOf()

        sharedPrefs[Constant.PREF_KEY_USER_SURVEYS] = ""

        return@withContext
    }

    /**
     * 현재 문제를 마킹하고 다음으로 넘어간다.
     *
     * @return 다음으로 풀어햐 하는 [설문 정보][SurveyInfo]를 리턴한다.
     *         이미 문제를 다 푼 상태라면 null 을 리턴한다.
     */
    suspend fun markingSurvey(
        selectedAnswer: SurveyAnswerType
    ): SurveyInfo? = withContext(Dispatchers.IO) {
        /** */
        if (cursor >= surveys.size) return@withContext null

        surveys = surveys.toMutableList()
            .apply { this[cursor] = this[cursor].copy(selectedAnswer = selectedAnswer) }
        cursor += 1

        saveSurveys(surveys)

        return@withContext getCurrentSurvey()
    }

    /**
     * 가장 먼저 해야할 설문의 인덱스 정보를 가져온다.
     * 설문을 중도에 그만두었을 경우, 다시 시작하는 지점을 찾기 위해 활용되며 [load] 에서만 사용한다.
     */
    suspend fun findSurveyCursor(): Int = withContext(Dispatchers.IO) {
        return@withContext surveys.indexOfFirst { it.selectedAnswer == SurveyAnswerType.UNKNOWN }
    }
}
