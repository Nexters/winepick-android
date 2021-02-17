package kr.co.nexters.winepick.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserInfoData(
    @SerialName("createdAt")
    val createdAt : String? = null,
    @SerialName("updatedAt")
    val updatedAt : String? = null,
    @SerialName("id")
    val id : Int? = null,
    @SerialName("accessToken")
    val accessToken : String? = null,
    @SerialName("personalityType")
    val personalityType : String? = null
)