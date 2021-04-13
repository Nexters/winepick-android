package kr.co.nexters.winepick.data.repository

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.model.*
import kr.co.nexters.winepick.data.source.SurveyDataSource
import kr.co.nexters.winepick.util.SharedPrefs
import javax.inject.Inject

class SurveyRepository @Inject constructor(
    private val surveyDataSource: SurveyDataSource,
    private var sharedPrefs: SharedPrefs
) {
    /** 인메모리에 저장되어 있는 설문 목록. 해당 값은 로깅 또는 참조용으로만 사용된다. */
    private val immutableSurveys: List<Survey>
        get() = Json.decodeFromString<List<Survey>>(
            sharedPrefs[Constant.PREF_KEY_USER_SURVEYS, ""] ?: ""
        ).map { it.copy(selectedAnswer = SurveyAnswerType.UNKNOWN) }

    /** 설문 목록 */
    private var surveys: List<Survey> = listOf()

    /** 현재 유저가 풀고 있는 문제의 index (번호 아님) */
    private var cursor: Int = 0

    /** 처음 앱 실행 시, 앱 내에 저장되어 있는 설문 목록울 가져온다. */
    suspend fun load() = withContext(Dispatchers.IO) {
        val inMemorySurveys = Json.decodeFromString<List<Survey>>(
            sharedPrefs[Constant.PREF_KEY_USER_SURVEYS, "[]"] ?: "[]"
        )

        surveys = inMemorySurveys
        cursor = findSurveyCursor
    }

    /** 새로운 설문 정보를 가져온다. */
    suspend fun resetSurvey() = withContext(Dispatchers.IO) {
        val response = surveyDataSource.getSurvey()

        val newSurveys = response?.result ?: listOf()
        saveSurveys(newSurveys)

        surveys = newSurveys
        cursor = 0
    }

    /**
     * 현재 풀어야 하는 인덱스 및 설문 정보를 가져온다.
     *
     * 문제를 다 푼 상태인 경우 맨 마지막에 답변한 내용과 -1 을 리턴한다.
     * 문제를 다 안풀었는데 풀어야 할 인덱스를 알 수 없는 경우 null 을 리턴한다.
     */
    fun getCurrentSurvey(): SurveyInfo? = if (cursor < surveys.size && cursor > -1)
        SurveyInfo(cursor, surveys[cursor])
    else if (!surveys.isNullOrEmpty() && isAllSurveyAnswered) {
        SurveyInfo(-1, surveys.last())
    } else
        null

    /**
     * 앱 내에 새롭게 설문한 내용을 저장한다.
     *
     * @param surveys 새롭게 저장할 설문 목록
     */
    private fun saveSurveys(surveys: List<Survey>) {
        val jsonString = Json.encodeToString(surveys)

        sharedPrefs[Constant.PREF_KEY_USER_SURVEYS] = jsonString
    }

    /** 앱 내에 저장된 설문 목록을 지우고 싶을 시 호출한다. */
    suspend fun clearSurveys() = withContext(Dispatchers.IO) {
        surveys = listOf()

        sharedPrefs[Constant.PREF_KEY_USER_SURVEYS] = "[]"

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
        if (cursor >= surveys.size) return@withContext null

        surveys = surveys.toMutableList()
            .apply { this[cursor] = this[cursor].copy(selectedAnswer = selectedAnswer) }
        cursor += 1

        // 현재 3번 문항까지 풀었다면 외향형, 내향형 문제를 구분한다. (4~6 외향형, 7~9 내향형)
        if (cursor == 3) {
            // 1차 분석 (1~3)
            val personVersion = getPeopleVersion()

            // 설문 데이터 조립
            surveys = when (personVersion) {
                PersonVersion.INTROVERSION -> {
                    surveys.take(3) + surveys.subList(6, 9)
                }
                PersonVersion.EXTROVERSION -> {
                    surveys.take(3) + surveys.subList(3, 6)
                }
                else -> surveys
            }
        }
        // 모든 문제를 풀었다면 응답 내용을 기반으로 최종 성향을 구분한다.
        else if (cursor == 6) {
            val personType = getPersonality()

            sharedPrefs[Constant.PREF_KEY_INT_USER_SURVEY_RESULT] = personType
        }

        // 현재까지 진행한 내용 저장
        // TODO 현대 3번까지 풀면 앱 내에 저장되는 데이터가 바뀌게 됨
        //      만약 이전 문제로 돌아가는 기능이 생길경우 해당 로직 수정 필요!!!
        saveSurveys(surveys)

        return@withContext getCurrentSurvey()
    }

    /** 사람의 성향을 분석한다. */
    private fun getPeopleVersion(): PersonVersion {
        val analysis = surveys.take(3)
            .filter { it.selectedAnswer == SurveyAnswerType.ANSWER_A }

        // 사람의 성향값 뽑아내기
        return when {
            (0..1).contains(analysis.size) -> PersonVersion.INTROVERSION   // 내향형
            (2..3).contains(analysis.size) -> PersonVersion.EXTROVERSION   // 외향형
            else -> PersonVersion.UNKNOWN
        }
    }

    /** 사람의 최종 성향을 분석한다. */
    private fun getPersonality(): Int {
        val analysis = surveys.subList(3, 6)
            .joinToString(separator = "") { it.selectedAnswer.parse() }

        return when (getPeopleVersion()) {
            PersonVersion.INTROVERSION -> when (analysis) {
                "AAA", "BAA", "ABA", "BBA" -> 1
                "AAB", "BAB" -> 2
                "ABB", "BBB" -> 3
                else -> 0
            }
            PersonVersion.EXTROVERSION -> when (analysis) {
                "AAA", "ABA" -> 6
                "AAB", "ABB" -> 5
                "BAA", "BAB" -> 4
                "BBA", "BBB" -> 3
                else -> 0
            }
            else -> 0
        }
    }

    /**
     * 가장 먼저 해야할 설문의 인덱스 정보를 가져온다.
     * 설문을 중도에 그만두었을 경우, 다시 시작하는 지점을 찾기 위해 활용되며 [load] 에서만 사용한다.
     */
    val findSurveyCursor: Int
        get() = surveys.indexOfFirst { it.selectedAnswer == SurveyAnswerType.UNKNOWN }

    /** 모든 문항에 답변을 완료하였는지 확인한다. */
    val isAllSurveyAnswered: Boolean
        get() = surveys.filter { it.selectedAnswer == SurveyAnswerType.UNKNOWN }.isNullOrEmpty()
}
