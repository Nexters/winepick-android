package kr.co.nexters.winepick.test

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ktor 을 테스트하기 위한 더미 응답 데이터 클래스
 *
 * @since v1.0.0 / 2021.02.02
 */
@Serializable
data class DummyMovieCommentResponse(
    @SerialName("rating")
    val rating: Int,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("writer")
    val writer: String,
    @SerialName("movie_id")
    val movie_id: String,
    @SerialName("contents")
    val contents: String,
    @SerialName("id")
    val id: String,
)
