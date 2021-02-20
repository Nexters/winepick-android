package kr.co.nexters.winepick.network

import kr.co.nexters.winepick.data.model.*
import kr.co.nexters.winepick.data.model.remote.wine.WineResult
import kr.co.nexters.winepick.data.model.remote.wine.WinesResult
import kr.co.nexters.winepick.data.response.PersonalityType
import kr.co.nexters.winepick.data.response.getUserData
import kr.co.nexters.winepick.data.response.postUserData
import retrofit2.Call
import retrofit2.http.*

/**
 * 실제 서비스에서 사용하는 Retrofit2 인터페이스
 *
 * @since v1.0.0 / 2021.02.04
 */
interface WinePickService {
    @GET("v2/api/wine")
    fun getWines(
        @Header("Authorization") authorization: String,
        @Query("pageSize") pageSize: Int,       // 한번에 가져올 사이즈
        @Query("pageNumber") pageNumber: Int?,  // 해당 페이지
        @Query("sort") sort: String             // id 기준으로 내림차순/오름차순 정렬 가능 유무
    ): Call<WinePickResponse<WinesResult>>

    @GET("v2/api/wine/{wine_id}")
    fun getWine(
        @Header("Authorization") authorization: String,
        @Path("wine_id") wineId: Int,           // wineId (1, 2, 3...)
    ): Call<WinePickResponse<WineResult>>

    @GET("v2/api/wine/filter{query}")
    fun getWinesFilter(
        @Header("Authorization") authorization: String,
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
    ): Call<WinePickResponse<List<WineList>>>

    @PUT("v2/api/like/{userId}/{wineId}")
    fun deleteLike(
        @Path("userId") userId: Int,
        @Path("wineId") wineId: Int
    ): Call<WinePickResponse<Unit>>

    /**
     * addUser
     */
    @POST("v2/api/user/")
    fun postUser(
        @Body data: UserData
    ): Call<WinePickResponse<postUserData>>

    @GET("v2/api/user/me/{accessToken}")
    fun getUser(
        @Path("accessToken") accessToken: String
    ): Call<WinePickResponse<getUserData>>

    /**
     * getUserPersonality
     */
    @GET("v2/api/result/{resultId}")
    fun getResult(
        @Path("resultId") resultId: Int
    ): Call<WinePickResponse<PersonalityType>>
}
