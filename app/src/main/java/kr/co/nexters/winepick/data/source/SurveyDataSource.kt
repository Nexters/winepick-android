package kr.co.nexters.winepick.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.data.response.SurveyResponse
import kr.co.nexters.winepick.network.NetworkModules
import kr.co.nexters.winepick.network.NetworkModules.send

object SurveyDataSource {
    /** [WinePickService.getSurvey] 요청에 대한 비즈니스 로직 */
    suspend fun getSurvey(): Survey? = withContext(Dispatchers.IO) {
        val response = NetworkModules.retrofit.getSurveys().send()
        return@withContext response.body()
    }

}