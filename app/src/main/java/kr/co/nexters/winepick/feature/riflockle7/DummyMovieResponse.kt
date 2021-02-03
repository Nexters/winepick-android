package kr.co.nexters.winepick.feature.riflockle7

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ktor 을 테스트하기 위한 더미 응답 데이터 클래스
 *
 * @since v1.0.0 / 2021.02.02
 */
@Serializable
data class DummyMovieResponse(
    @SerialName("movies")
    val movies: List<DummyMovie>,
    @SerialName("order_type")
    val orderType: Int
)
