package kr.co.nexters.winepick.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * WinePick 기본 response 구조
 *
 * @since v1.0.0 / 2021.02.02
 */
@Serializable
data class WinePickResponse<T>(
    @SerialName("data")
    val result: T,

    @SerialName("message")
    val message: String,

    @SerialName("statusCode")
    val statusCode: Int
)
