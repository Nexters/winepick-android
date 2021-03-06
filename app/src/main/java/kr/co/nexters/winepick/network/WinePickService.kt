package kr.co.nexters.winepick.network

import kr.co.nexters.winepick.data.model.*
import kr.co.nexters.winepick.data.model.remote.user.UserResult
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.WinesResult
import kr.co.nexters.winepick.data.response.PersonalityType
import kr.co.nexters.winepick.data.response.UserData
import retrofit2.Call
import retrofit2.http.*

/**
 * 실제 서비스에서 사용하는 Retrofit2 인터페이스
 *
 * @since v1.0.0 / 2021.02.04
 */
interface WinePickService {
    @GET("v2/api/survey/")
    fun getSurveys(): Call<WinePickResponse<List<Survey>>>

    @GET("v2/api/wine")
    fun getWines(
        @Query("size") size: Int,       // 한번에 가져올 사이즈
        @Query("page") page: Int?,      // 해당 페이지
        @Query("sort") sort: String     // id 기준으로 내림차순/오름차순 정렬 가능 유무
    ): Call<WinePickResponse<WinesResult>>

    @GET("v2/api/wine/keyword")
    fun getWinesKeyword(
        @Query("size") size: Int,       // 한번에 가져올 사이즈
        @Query("page") page: Int?,      // 해당 페이지
        @Query("keyword") sort: String  // 키워드 정보
    ): Call<WinePickResponse<WinesResult>>

    @GET("v2/api/wine/{wine_id}")
    fun getWine(
        @Path("wine_id") wineId: Int,           // wineId (1, 2, 3...)
    ): Call<WinePickResponse<WineResult>>

    @GET("v2/api/wine/filter{query}")
    fun getWinesFilter(
        @Path(value = "query", encoded = false) query: String,
        // wineName : 와인 이름 영문/한글 상관 없음
        // category : 화이트/레드/스파클링
        // food : 음식
        // store : 편의점
        // start : 도수 시작점
        // end : 도수 끝
        // keyword : 키워드
        // size : 한번에 가져올 사이즈
        // page : 해당 페이지
        // sort : id 기준으로 내림차순/오름차순 정렬 가능 유무
    ): Call<WinePickResponse<WinesResult>>

    /**
     * updateUser
     */
    @POST("v2/api/user/accessToken")
    fun postUserAccessToken(
        @Body data: TokenInfo
    ): Call<WinePickResponse<Unit>>

    /**
     * updateUser
     */
    @PUT("v2/api/user/me/{accessToken}")
    fun putUser(
        @Path("accessToken") accessToken: String,
        @Body data: PutUserRequest
    ): Call<WinePickResponse<UserResult>>

    /**
     * addLike
     */
    @POST("v2/api/like")
    fun postLike(
        @Body data: LikeWine
    ): Call<WinePickResponse<Unit>>

    /**
     * getLikesWineList
     */
    @GET("v2/api/like/{userId}")
    fun getLikesWineList(
        @Path("userId") userId: Int
    ): Call<WinePickResponse<List<WineResult>>>

    @DELETE("v2/api/like/{userId}/{wineId}")
    fun deleteLike(
        @Path("userId") userId: Int,
        @Path("wineId") wineId: Int
    ): Call<WinePickResponse<Unit>>

    /**
     * addUser
     */
    @POST("v2/api/user")
    fun postUser(
        @Body data: AccessTokenData
    ): Call<WinePickResponse<UserData>>

    @GET("v2/api/user/{userId}/{accessToken}")
    fun getUser(
        @Path("userId") userId: Int,
        @Path("accessToken") accessToken: String
    ): Call<WinePickResponse<UserData>>

    /**
     * getUserPersonality
     */
    @GET("v2/api/result/{resultId}")
    fun getResult(
        @Path("resultId") resultId: Int
    ): Call<WinePickResponse<PersonalityType>>
}
