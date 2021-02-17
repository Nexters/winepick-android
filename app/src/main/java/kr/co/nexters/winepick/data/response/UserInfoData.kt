package kr.co.nexters.winepick.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*


data class UserInfoData(
    @SerializedName("createdAt")
    val createdAt : String? = null,
    @SerializedName("updatedAt")
    val updatedAt : String? = null,
    @SerializedName("id")
    val id : Int? = null,
    @SerializedName("accessToken")
    val accessToken : String? = null,
    @SerializedName("personalityType")
    val personalityType : String? = null
)