package kr.co.nexters.winepick.api.survey

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.runBlocking
import kr.co.nexters.winepick.base.AndroidBaseTest
import kr.co.nexters.winepick.data.constant.Constant
import kr.co.nexters.winepick.data.source.SurveyDataSource
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * 설문 관련 api 테스트
 *
 * @since v1.0.0 / 2021.03.18
 */
class SurveyApiTest : AndroidBaseTest() {
    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    /** [SurveyDataSource.getSurvey] 테스트 */
    @Test
    fun getSurveyTest() = runBlocking {
        surveyRepository.resetSurvey()

        val currentSurveyInfo = surveyRepository.getCurrentSurvey()

        Assert.assertEquals(currentSurveyInfo?.number, 0)

        Assert.assertTrue(!sharedPrefs[Constant.PREF_KEY_USER_SURVEYS, ""].isNullOrEmpty())
    }
}
