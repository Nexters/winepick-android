package kr.co.nexters.winepick.data.model

import com.google.gson.annotations.SerializedName
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
    @SerializedName("data")
    val result: T,

    @SerializedName("message")
    @SerialName("message")
    val message: String,

    @SerializedName("statusCode")
    @SerialName("statusCode")
    val statusCode: Int
)
