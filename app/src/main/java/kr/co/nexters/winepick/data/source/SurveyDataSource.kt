package kr.co.nexters.winepick.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.data.model.WinePickResponse
import kr.co.nexters.winepick.di.send
import kr.co.nexters.winepick.network.WinePickService
import javax.inject.Inject

class SurveyDataSource @Inject constructor(private val winePickService: WinePickService) {
    /** [WinePickService.getSurveys] 요청에 대한 비즈니스 로직 */
    suspend fun getSurvey(): WinePickResponse<List<Survey>>? = withContext(Dispatchers.IO) {
        val response = winePickService.getSurveys().send()
        return@withContext response.body()
    }
}
