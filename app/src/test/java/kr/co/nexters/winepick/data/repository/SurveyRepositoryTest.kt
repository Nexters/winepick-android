package kr.co.nexters.winepick.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.data.model.SurveyAnswerType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * [SurveyRepository] 를 활용하는 시나리오 테스트
 *
 * @since v1.0.0 / 2021.03.18
 */
class SurveyRepositoryTest : AndroidBaseTest() {
    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    /** 직접 설문 목록을 새로 받아 푸는 과정을 진행한다. */
    @Test
    fun markingSurveyTest() = runBlocking {
        // 문제를 새로 받는다.
        surveyRepository.resetSurvey()

        // 현재 풀어야 하는 문제의 정보를 확인한다.
        val currentSurveyInfo = surveyRepository.getCurrentSurvey()

        // 이 문제는 0번이어야 한다.
        Assert.assertEquals(currentSurveyInfo?.number, 0)

        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        val checkSurveyInfo = surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)

        // 지금 4번까지 풀었으므로 index 값은 4여야 한다. (5번 문제)
        Assert.assertEquals(checkSurveyInfo?.number, 4)

        // 실제 앱 내에 저장된 데이터에서도 index 값은 4여야 한다는 걸 알고 있어야 한다. (5번 문제)
        Json.decodeFromString<List<Survey>>(sharedPrefs[Constant.PREF_KEY_USER_SURVEYS, ""]!!).run {
            Assert.assertEquals(
                indexOfFirst { it.selectedAnswer == SurveyAnswerType.UNKNOWN },
                4
            )
        }

        // 많은 문제를 푼다 (일부로 적정량보다 많은 문제를 풀도록 만들어 crash 를 유도한다.)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_B)
        surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)
        val mustNullInfo = surveyRepository.markingSurvey(SurveyAnswerType.ANSWER_A)

        // 문제를 다 푼 상태일 것이므로 null 이어야 한다.
        Assert.assertNull(mustNullInfo)

        // 실제 앱 내에 저장된 데이터에서도 문제를 다 푼 상태일 것이므로 -1 이 나와야 한다.
        Json.decodeFromString<List<Survey>>(sharedPrefs[Constant.PREF_KEY_USER_SURVEYS, ""]!!).run {
            Assert.assertEquals(
                indexOfFirst { it.selectedAnswer == SurveyAnswerType.UNKNOWN },
                -1
            )
        }
    }
}
