@file:UseContextualSerialization(URL::class)
@file:UseSerializers(URLSerializer::class)

package kr.co.nexters.winepick.test

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import kotlinx.serialization.UseSerializers
import java.net.URL

/**
 * ktor 을 테스트하기 위한 더미 데이터 클래스
 *
 * @since v1.0.0 / 2021.02.02
 */
@Serializable
data class DummyDetailMovie(
    @SerialName("actor")
    val actor: String,
    @SerialName("audience")
    val audience: Int,
    @SerialName("date")
    val date: String,
    @SerialName("director")
    val director: String,
    @SerialName("duration")
    val duration: Int,
    @SerialName("genre")
    val genre: String,
    @SerialName("grade")
    val grade: Int,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: URL?,
    @SerialName("reservation_grade")
    val reservationGrade: Int,
    @SerialName("reservation_rate")
    val reservationRate: Float,
    @SerialName("synopsis")
    val synopsis: String,
    @SerialName("title")
    val title: String,
    @SerialName("user_rating")
    val userRating: Int
)
