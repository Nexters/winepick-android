package kr.co.nexters.winepick.network

import kr.co.nexters.winepick.data.model.Survey
import kr.co.nexters.winepick.data.response.SurveyResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * 실제 서비스에서 사용하는 Retrofit2 인터페이스
 *
 * @author ricky
 * @since v1.0.0 / 2021.02.04
 */
interface WinePickService {
    @GET("v2/api/survey/")
    fun getSurveys(): Call<Survey>
}
